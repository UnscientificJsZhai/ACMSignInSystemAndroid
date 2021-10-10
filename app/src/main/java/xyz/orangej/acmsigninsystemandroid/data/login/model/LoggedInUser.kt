package xyz.orangej.acmsigninsystemandroid.data.login.model

import xyz.orangej.acmsigninsystemandroid.data.login.LoginRepository

/**
 * 为已登录用户保存数据的数据类。
 *
 * @param userId Session。
 * @param displayName 显示名称。
 * @see LoginRepository
 */
data class LoggedInUser(
    val userId: String,
    val displayName: String
)