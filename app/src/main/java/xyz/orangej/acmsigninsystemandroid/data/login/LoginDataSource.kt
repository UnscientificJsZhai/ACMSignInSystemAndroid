package xyz.orangej.acmsigninsystemandroid.data.login

import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import xyz.orangej.acmsigninsystemandroid.api.HttpApi
import xyz.orangej.acmsigninsystemandroid.data.login.model.LoggedInUser
import xyz.orangej.acmsigninsystemandroid.util.string
import java.io.IOException
import javax.inject.Inject

/**
 * 处理登录和身份认证的类。
 */
class LoginDataSource @Inject constructor(private val client: HttpApi) {

    /**
     * 登录的实现。
     *
     * @param username 用户名。
     * @param password 密码。
     */
    @WorkerThread
    fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            val responseBody = runBlocking { client.login(username, password).string() }
            Log.e("LoginDataSource", "login: $responseBody")
            val responseJsonObject =
                JSONObject(
                    responseBody
                ).getJSONObject("data")
            val user = LoggedInUser(
                userId = responseJsonObject.getString("sessionId"),
                displayName = responseJsonObject.getString("username")
            )
            Result.Success(user)
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }
}