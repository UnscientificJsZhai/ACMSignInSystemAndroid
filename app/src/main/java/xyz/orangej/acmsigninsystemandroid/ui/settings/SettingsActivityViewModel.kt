package xyz.orangej.acmsigninsystemandroid.ui.settings

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject
import xyz.orangej.acmsigninsystemandroid.api.callCheckApi
import xyz.orangej.acmsigninsystemandroid.api.callGetTrainingHistory
import xyz.orangej.acmsigninsystemandroid.api.callLogout
import xyz.orangej.acmsigninsystemandroid.data.user.TrainingRecord

class SettingsActivityViewModel : ViewModel() {

    companion object {

        const val TAG = "SettingsActivityViewModel"
    }

    /**
     * 处理各种网络请求的Client。
     */
    private val httpClient = OkHttpClient()

    /**
     * 获取训练数据。
     *
     * @param context 用于获取服务器地址。
     * @param session 当前登录用户的Session。
     * @return 新增的训练记录列表。
     */
    suspend fun getTrainHistory(context: Context, session: String): List<TrainingRecord> {
        val jsonString =
            withContext(Dispatchers.IO) {
                try {
                    this@SettingsActivityViewModel.httpClient.callGetTrainingHistory(
                        context,
                        session,
                        0
                    )
                } catch (e: Exception) {
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
                            ""
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
     * 登出。
     *
     * @param context 用于获取服务器地址。
     * @param session 当前登录用户的Session。
     */
    suspend fun logout(context: Context, session: String) {
        withContext(Dispatchers.IO) {
            this@SettingsActivityViewModel.httpClient.callLogout(context, session)
        }
    }

    /**
     * 检查Api。
     *
     * @param server 服务器地址。
     * @return 服务器是否合法。
     */
    suspend fun checkApi(server:String): Boolean {
        val response = withContext(Dispatchers.IO) {
            try {
                this@SettingsActivityViewModel.httpClient.callCheckApi(server)
            } catch (e: Exception) {
                ""
            }
        }
        Log.e(TAG, "getTrainHistory: $response")
        try {
            JSONObject(response ?: "").also {
                return it.getString("status") == "success"
            }
        } catch (e: JSONException) {
            return false
        }
    }
}