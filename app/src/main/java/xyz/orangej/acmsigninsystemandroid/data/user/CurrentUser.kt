package xyz.orangej.acmsigninsystemandroid.data.user

/**
 * 已经登录的用户信息。
 *
 * @param sessionId 用户登录用到的session。
 * @param displayName 用户的显示名称。
 */
data class CurrentUser(val sessionId: String, val displayName: String)