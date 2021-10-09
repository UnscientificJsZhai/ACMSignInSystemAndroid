package xyz.orangej.acmsigninsystemandroid.ui.login

/**
 * Authentication result : success (UserActivity details) or error message.
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
) {

    /**
     * 展示给登录界面的用户数据。
     */
    data class LoggedInUserView(
        val displayName: String
    )
}
