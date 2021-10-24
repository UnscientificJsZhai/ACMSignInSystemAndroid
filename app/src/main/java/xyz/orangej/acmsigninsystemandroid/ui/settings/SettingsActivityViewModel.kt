package xyz.orangej.acmsigninsystemandroid.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject
import xyz.orangej.acmsigninsystemandroid.api.callGetTrainingHistory
import xyz.orangej.acmsigninsystemandroid.api.callLogout
import xyz.orangej.acmsigninsystemandroid.data.user.TrainingRecord
import xyz.orangej.acmsigninsystemandroid.ui.main.MainActivityViewModel

class SettingsActivityViewModel : ViewModel() {

    /**
     * 处理各种网络请求的Client。
     */
    private val httpClient = OkHttpClient()

    /**
     * 获取训练数据。
     *
     * @param session 当前登录用户的Session。
     * @return 新增的训练记录列表。
     */
    suspend fun getTrainHistory(session: String): List<TrainingRecord> {
        val jsonString =
            withContext(Dispatchers.IO) {
                try {
                    this@SettingsActivityViewModel.httpClient.callGetTrainingHistory(session, 0)
                } catch (e: Exception) {
                    null
                }
            } ?: return emptyList()
        Log.e(MainActivityViewModel.TAG, "getTrainHistory: $jsonString")
        val jsonObject = try {
            JSONObject(jsonString).also {
                assert(it.getString("status") == "success")
            }
        } catch (e: JSONException) {
            Log.e(MainActivityViewModel.TAG, "getTrainHistory: $e")
            return emptyList()
        } catch (e: AssertionError) {
            Log.e(MainActivityViewModel.TAG, "getTrainHistory: $e")
            return emptyList()
        }
        val array = try {
            jsonObject.getJSONObject("data").getJSONArray("records")
        } catch (e: JSONException) {
            Log.e(MainActivityViewModel.TAG, "getTrainHistory: $e")
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
     * @param session 当前登录用户的Session。
     */
    suspend fun logout(session: String) {
        withContext(Dispatchers.IO) {
            this@SettingsActivityViewModel.httpClient.callLogout(session)
        }
    }
}