@file:JvmName("ServerConfig")

package xyz.orangej.acmsigninsystemandroid.api

/**
 * 登录Url。
 *
 * @param server 输入存放在设置中的服务器地址。
 * @return 字符串格式的URL。
 */
fun loginURL(server: String) = "$server/api/login/"

/**
 * 注册Url。
 *
 * @param server 输入存放在设置中的服务器地址。
 * @return 字符串格式的URL。
 */
fun registerURL(server: String) = "$server/api/register/"

/**
 * 登出Url。
 *
 * @param server 输入存放在设置中的服务器地址。
 * @return 字符串格式的URL。
 */
fun logoutURL(server: String) = "$server/api/logout/"

/**
 * 用户数据Url。
 *
 * @param server 输入存放在设置中的服务器地址。
 * @return 字符串格式的URL。
 */
fun userInfoURL(server: String) = "$server/api/getuserinfo/"

/**
 * 训练记录URL。
 *
 * @param server 输入存放在设置中的服务器地址。
 * @return 字符串格式的URL。
 */
fun trainingHistoryURL(server: String) = "$server/api/getrecord/"

/**
 * 获取制定的训练记录的URL。
 *
 * @param server 输入存放在设置中的服务器地址。
 * @return 字符串格式的URL。
 */
fun specificTrainingHistoryURL(server: String) = "$server/api/getspecificrecord/"

/**
 * 签到URL。
 *
 * @param server 输入存放在设置中的服务器地址。
 * @return 字符串格式的URL。
 */
fun signInURL(server: String) = "$server/api/signin/"

/**
 * 获取邮箱验证码的URL。
 *
 * @param server 输入存放在设置中的服务器地址。
 * @return 字符串格式的URL。
 */
fun getEmailCodeURL(server: String) = "$server/api/getemailcode/"

/**
 * 检查Api的URL。
 *
 * @param server 输入存放在设置中的服务器地址。
 * @return 字符串格式的URL。
 */
fun checkApiURL(server: String) = "$server/api/"