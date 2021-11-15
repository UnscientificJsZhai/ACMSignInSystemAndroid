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

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If UserFragment credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    @WorkerThread
    fun login(context: Context, username: String, password: String): Result<LoggedInUser> {
        // handle login
        val result = dataSource.login(context, username, password)

        if (result is Result.Success) {
            this.user = result.data
        }

        return result
    }
}