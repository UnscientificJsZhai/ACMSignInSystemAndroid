package xyz.orangej.acmsigninsystemandroid.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.orangej.acmsigninsystemandroid.data.login.LoginRepository

/**
 * 提供[LoginViewModel]的工厂类。
 *
 * @see LoginViewModel
 */
class LoginViewModelFactory(private val loginRepository: LoginRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(loginRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}