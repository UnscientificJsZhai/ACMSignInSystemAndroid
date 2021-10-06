@file:JvmName("ServerConfig")

package xyz.orangej.acmsigninsystemandroid.api

/**
 * 服务器地址。
 */
const val SERVER_ADDRESS = "https://www.orangej.xyz"

/**
 * 登录Url。
 *
 * @return 字符串格式的URL。
 */
fun loginURL() = "$SERVER_ADDRESS/api/login/"

/**
 * 注册Url。
 *
 * @return 字符串格式的URL。
 */
fun registerURL() = "$SERVER_ADDRESS/api/register/"

/**
 * 登出Url。
 *
 * @return 字符串格式的URL。
 */
fun logoutURL() = "$SERVER_ADDRESS/api/logout"

fun userInfoURL() = "$SERVER_ADDRESS/api/getuserinfo/"