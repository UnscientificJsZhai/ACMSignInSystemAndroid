package xyz.orangej.acmsigninsystemandroid.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject
import xyz.orangej.acmsigninsystemandroid.api.callGetSpecificTrainingHistory
import xyz.orangej.acmsigninsystemandroid.api.callGetTrainingHistory
import xyz.orangej.acmsigninsystemandroid.api.callGetUserInfo
import xyz.orangej.acmsigninsystemandroid.api.callSignIn
import xyz.orangej.acmsigninsystemandroid.data.user.CurrentUser
import xyz.orangej.acmsigninsystemandroid.data.user.TrainingRecord
import xyz.orangej.acmsigninsystemandroid.data.user.database.UserDao
import java.io.IOException

/**
 * MainActivity的ViewModel。
 *
 * @see MainActivity
 */
class MainActivityViewModel(
    var currentUser: LiveData<CurrentUser>,
    var trainingRecords: LiveData<List<TrainingRecord>>
) : ViewModel() {

    companion object {

        const val TAG = "MainActivityViewModel"
    }

    /**
     * 封装对当前登录用户请求的结果。
     */
    sealed class GetUserResult {

        class Success(val data: CurrentUser) : GetUserResult()
        class Error(val exception: Throwable) : GetUserResult()
    }

    /**
     * 处理各种网络请求的Client。
     */
    private val httpClient = OkHttpClient()

    /**
     * 初始化此ViewModel的工厂。
     *
     * @param dao 操作数据库使用的dao对象。
     * @param sessionHash 当前登录用户的Session的HashCode。
     */
    class Factory(private val dao: UserDao, private val sessionHash: Int) :
        ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainActivityViewModel(
                dao.getCurrentUser(sessionHash),
                dao.getTrainingRecords()
            ) as T
        }
    }

    /**
     * 获取当前用户信息。如果返回null则为登录失效。
     *
     * @param session
     */
    suspend fun getCurrentUser(session: String): GetUserResult {
        val jsonString = try {
            withContext(Dispatchers.IO) {
                this@MainActivityViewModel.httpClient.callGetUserInfo(session)
            } ?: return GetUserResult.Error(RuntimeException("空返回结果"))
        } catch (e: Exception) {
            return GetUserResult.Error(e)
        }
        Log.e(TAG, "getCurrentUser: $jsonString")
        val jsonObject = try {
            JSONObject(jsonString).also {
                assert(it.getString("status") == "success")
            }
        } catch (e: JSONException) {
            Log.e(TAG, "getTrainHistory: $e")
            return GetUserResult.Error(e)
        } catch (e: AssertionError) {
            Log.e(TAG, "getTrainHistory: $e")
            return GetUserResult.Error(e)
        }
        Log.e(TAG, jsonString)

        return try {
            val item = jsonObject.getJSONObject("data")
            GetUserResult.Success(
                CurrentUser(
                    sessionHash = session.hashCode(),
                    userName = item.getString("username"),
                    displayName = item.getString("name"),
                    email = item.getString("email"),
                    department = item.getString("department"),
                    major = item.getString("major"),
                    joinTime = item.getString("joinTime"),
                    allTrainingTime = item.getString("allTrainningTime")
                )
            )
        } catch (e: JSONException) {
            Log.e(TAG, "getCurrentUser: $e")
            GetUserResult.Error(e)
        }
    }

    /**
     * 获取训练数据。
     *
     * @param session 当前登录用户的Session。
     * @param startAt 起始ID。为0则为获取全部。
     * @return 新增的训练记录列表。
     */
    suspend fun getTrainHistory(session: String, startAt: Int = 0): List<TrainingRecord> {
        val jsonString =
            withContext(Dispatchers.IO) {
                try {
                    this@MainActivityViewModel.httpClient.callGetTrainingHistory(session, startAt)
                } catch (e: java.lang.Exception) {
                    null
                }
            } ?: return emptyList()
        Log.e(TAG, "getTrainHistory: $jsonString")
        val jsonObject = try {
            JSONObject(jsonString).also {
                assert(it.getString("status") == "success")
            }
        } catch (e: JSONException) {
            Log.e(TAG, "getTrainHistory: $e")
            return emptyList()
        } catch (e: AssertionError) {
            Log.e(TAG, "getTrainHistory: $e")
            return emptyList()
        }
        val array = try {
            jsonObject.getJSONObject("data").getJSONArray("records")
        } catch (e: JSONException) {
            Log.e(TAG, "getTrainHistory: $e")
            return emptyList()
        }

        val list = ArrayList<TrainingRecord>()

        for (index in 0..array.length()) {
            try {
                val item = array.getJSONObject(index)
                val statusCode = item.getInt("status")
                list.add(
                    TrainingRecord(
                        sessionHash = session.hashCode(),
                        id = item.getLong("id"),
                        userName = item.getString("username"),
                        startTime = item.getString("startTime"),
                        endTime = if (statusCode == 0) {
                            "正在训练中"
                        } else {
                            item.getString("endTime")
                        },
                        status = statusCode,
                        timeLength = item.getString("timeLength")
                    )
                )
            } catch (e: JSONException) {
                continue
            }
        }

        return list
    }

    /**
     * 获取特定的训练数据。这常被用来检查之前的不通过数据现在是否通过了。
     *
     * @param session 当前登录用户的Session。
     * @param id 要重新获取的记录的ID。
     * @return 特定的训练记录。
     */
    suspend fun getSpecificTrainingHistory(session: String, id: Long): TrainingRecord? {
        val jsonString = withContext(Dispatchers.IO) {
            try {
                this@MainActivityViewModel.httpClient.callGetSpecificTrainingHistory(session, id)
            } catch (e: IOException) {
                null
            }
        } ?: return null
        Log.e(TAG, "getTrainHistory: $jsonString")
        val jsonObject = try {
            JSONObject(jsonString).also {
                assert(it.getString("status") == "success")
            }
        } catch (e: JSONException) {
            Log.e(TAG, "getTrainHistory: $e")
            return null
        } catch (e: AssertionError) {
            Log.e(TAG, "getTrainHistory: $e")
            return null
        }
        val item = try {
            jsonObject.getJSONObject("data").getJSONObject("record")
        } catch (e: JSONException) {
            Log.e(TAG, "getTrainHistory: $e")
            return null
        }

        if (item.isNull("status")) {
            //服务器已经删除，则直接返回
            return null
        } else {
            val statusCode = item.getInt("status")
            return try {
                TrainingRecord(
                    sessionHash = session.hashCode(),
                    id = item.getLong("id"),
                    userName = item.getString("username"),
                    startTime = item.getString("startTime"),
                    endTime = if (statusCode == 0) {
                        "正在训练中"
                    } else {
                        item.getString("endTime")
                    },
                    status = statusCode,
                    timeLength = item.getString("timeLength")
                )
            } catch (e: JSONException) {
                null
            }
        }
    }

    /**
     * 登录结果的返回信息。
     */
    sealed class SignInResult {

        data class Success(val message: SuccessType) : SignInResult()
        data class Error(val errorCode: ErrorCode) : SignInResult()

        enum class SuccessType {
            START, END
        }

        enum class ErrorCode {
            NETWORK_ERROR, ILLEGAL_DATA, FAIL_TO_SIGN_IN
        }
    }

    /**
     * 登录。
     *
     * @param csrfToken
     * @param token
     * @param time
     * @return 服务器响应结果。
     */
    suspend fun signIn(
        session: String,
        csrfToken: String,
        token: String,
        time: String
    ): SignInResult {
        val response = withContext(Dispatchers.IO) {
            try {
                this@MainActivityViewModel.httpClient.callSignIn(session, csrfToken, token, time)
            } catch (e: IOException) {
                ""
            }
        }
        Log.e(TAG, "signIn: $response")
        if (response == null || response == "") {
            return SignInResult.Error(SignInResult.ErrorCode.NETWORK_ERROR)
        } else {
            val json = try {
                JSONObject(response)
            } catch (e: JSONException) {
                return SignInResult.Error(SignInResult.ErrorCode.ILLEGAL_DATA)
            }
            try {
                assert("success" == json.getString("status"))
                when (json.getString("msg")) {
                    "签到成功" -> return SignInResult.Success(SignInResult.SuccessType.START)
                    "签退成功" -> return SignInResult.Success(SignInResult.SuccessType.END)
                }
                return SignInResult.Error(SignInResult.ErrorCode.ILLEGAL_DATA)
            } catch (e: JSONException) {
                return SignInResult.Error(SignInResult.ErrorCode.ILLEGAL_DATA)
            } catch (e: AssertionError) {
                return SignInResult.Error(SignInResult.ErrorCode.FAIL_TO_SIGN_IN)
            }
        }
    }
}