package xyz.orangej.acmsigninsystemandroid.data.login

import android.content.Context
import androidx.annotation.WorkerThread
import xyz.orangej.acmsigninsystemandroid.data.login.model.LoggedInUser

/**
 * 用于从服务器请求用户信息和身份验证，并把登录状态和用户信息缓存在内存中的类。
 *
 * @param dataSource 数据源。
 */
class LoginRepository(private val dataSource: LoginDataSource) {

    var user: LoggedInUser? = null
        private set

    @WorkerThread
    fun login(context: Context, username: String, password: String): Result<LoggedInUser> {
        val result = dataSource.login(context, username, password)

        if (result is Result.Success) {
            this.user = result.data
        }

        return result
    }
}