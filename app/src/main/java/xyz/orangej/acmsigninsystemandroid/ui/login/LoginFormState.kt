package xyz.orangej.acmsigninsystemandroid.ui.login

/**
 * 登录界面的用户名密码表单的合法性检测相关数据类。
 */
data class LoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)