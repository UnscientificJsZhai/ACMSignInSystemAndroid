package xyz.orangej.acmsigninsystemandroid.util

import okhttp3.ResponseBody
import retrofit2.Response

/**
 * 将Retrofit响应转换为字符串。
 *
 * @receiver Retrofit响应结果。
 * @return 响应的字符串。如果响应body为null，则返回空字符串而不是null。
 */
fun Response<ResponseBody>.string() = this.body()?.string() ?: ""

/**
 * 格式化的SessionID参数。
 *
 * @param session Session的ID。
 * @return 格式化后的SessionID。格式化后才可传入Retrofit请求方法作为session参数。
 */
fun formattedSession(session: String) = "sessionid=$session"