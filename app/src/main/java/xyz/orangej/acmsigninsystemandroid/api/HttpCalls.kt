@file:JvmName("HttpCalls")

package xyz.orangej.acmsigninsystemandroid.api

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

private val mediaType: MediaType
    get() = MediaType.parse("application/x-www-form-urlencoded")!!

/**
 * 为Request添加Session。
 *
 * @return RequestBuilder用于链式调用。
 */
fun Request.Builder.addSession(session: String): Request.Builder =
    addHeader("Cookie", "sessionid=$session")

/**
 * 进行登录请求。
 *
 * @param userName 用户名。
 * @param password 密码。
 * @return 响应结果json字符串。
 */
fun OkHttpClient.callLogin(userName: String, password: String): String? {
    val requestBody = RequestBody.create(
        mediaType,
        "username=$userName&password=$password"
    )
    val request = Request.Builder()
        .url(loginURL())
        .method("POST", requestBody)
        .build()

    val call = this.newCall(request)
    val response = call.execute()
    return response.body()?.string()
}

/**
 * 进行登出请求。
 *
 * @param session 用户的Session。
 * @return 响应结果json字符串。
 */
fun OkHttpClient.callLogout(session: String): String? {
    val requestBody = RequestBody.create(
        mediaType,
        ""
    )
    val request = Request.Builder()
        .url(logoutURL())
        .addSession(session)
        .method("POST", requestBody)
        .build()

    val call = this.newCall(request)
    val response = call.execute()
    return response.body()?.string()
}

fun OkHttpClient.callGetUserInfo(session: String): String? {
    val requestBody = RequestBody.create(
        mediaType,
        ""
    )
    val request = Request.Builder()
        .url(userInfoURL())
        .addSession(session)
        .method("POST", requestBody)
        .build()

    val call = this.newCall(request)
    val response = call.execute()
    return response.body()?.string()
}