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

/**
 * 用户数据Url。
 *
 * @return 字符串格式的URL。
 */
fun userInfoURL() = "$SERVER_ADDRESS/api/getuserinfo/"

/**
 * 训练记录URL。
 *
 * @return 字符串格式的URL。
 */
fun trainingHistoryURL() = "$SERVER_ADDRESS/api/getrecord/"

/**
 * 获取制定的训练记录的URL。
 *
 * @return 字符串格式的URL。
 */
fun specificTrainingHistoryURL() = "$SERVER_ADDRESS/api/getspecificrecord/"