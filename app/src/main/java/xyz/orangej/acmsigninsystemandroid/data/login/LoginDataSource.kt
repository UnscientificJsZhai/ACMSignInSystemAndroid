package xyz.orangej.acmsigninsystemandroid.data.login

import androidx.annotation.WorkerThread
import xyz.orangej.acmsigninsystemandroid.data.login.model.LoggedInUser
import java.io.IOException
import java.util.*

/**
 * 处理登录和身份认证的类。
 */
class LoginDataSource {

    @WorkerThread
    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val user = LoggedInUser(UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(user)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}