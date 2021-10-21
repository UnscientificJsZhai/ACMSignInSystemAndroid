package xyz.orangej.acmsigninsystemandroid.data.login

import androidx.annotation.WorkerThread
import okhttp3.OkHttpClient
import org.json.JSONObject
import xyz.orangej.acmsigninsystemandroid.api.callLogin
import xyz.orangej.acmsigninsystemandroid.data.login.model.LoggedInUser
import java.io.IOException

/**
 * 处理登录和身份认证的类。
 */
class LoginDataSource {

    private val client by lazy {
        OkHttpClient()
    }

    /**
     * 登录的实现。
     *
     * @param username 用户名。
     * @param password 密码。
     */
    @WorkerThread
    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            val responseBody = client.callLogin(username, password)
            val responseJsonObject =
                JSONObject(
                    responseBody ?: return Result.Error(IOException("Empty response"))
                ).getJSONObject("data")
            val user = LoggedInUser(
                userId = responseJsonObject.getString("sessionId"),
                displayName = responseJsonObject.getString("username")
            )
            return Result.Success(user)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }
}