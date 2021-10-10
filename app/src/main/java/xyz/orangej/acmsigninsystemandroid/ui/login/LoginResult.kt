package xyz.orangej.acmsigninsystemandroid.ui.login

/**
 * 登录结果：成功则为用户名，失败则为错误信息。
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
