package xyz.orangej.acmsigninsystemandroid.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.orangej.acmsigninsystemandroid.BuildConfig
import xyz.orangej.acmsigninsystemandroid.R
import xyz.orangej.acmsigninsystemandroid.data.login.LoginRepository
import xyz.orangej.acmsigninsystemandroid.data.login.Result

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    companion object {

        /**
         * 检查用户名是否合法。
         *
         * @param username 用户名。
         * @return 合法性。
         */
        @JvmStatic
        fun isUserNameValid(username: String) = username.length in 4..16

        /**
         * 检查密码是否合法。
         *
         * @param password 密码。
         * @return 合法性。
         */
        @JvmStatic
        fun isPasswordValid(password: String): Boolean {
            if (BuildConfig.BUILD_TYPE == "debug") return true
            return if (password.length in 8..16) {
                var hasDigit = false
                var hasLetter = false
                for (char in password) {
                    if (char.isDigit()) {
                        hasDigit = true
                    } else if (char.isLetter()) {
                        hasLetter = true
                    }
                }
                hasDigit && hasLetter
            } else {
                false
            }
        }
    }

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    /**
     * 登录方法。
     *
     * @param username 用户名。
     * @param password 密码。
     * @return Session。
     */
    suspend fun login(context: Context, username: String, password: String): String {
        val result =
            withContext(Dispatchers.IO) { loginRepository.login(context, username, password) }

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoginResult.LoggedInUserView(displayName = result.data.displayName))
            return result.data.userId
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
        return ""
    }

    /**
     * 通知输入框中的用户名和密码发生变化，重新检查合法性。
     *
     * @param username 改变后的用户名。
     * @param password 改变后的密码。
     */
    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }
}