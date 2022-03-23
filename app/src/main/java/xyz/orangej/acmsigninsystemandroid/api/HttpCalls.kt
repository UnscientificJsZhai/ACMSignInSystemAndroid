@file:JvmName("HttpCalls")

package xyz.orangej.acmsigninsystemandroid.api

import android.content.Context
import android.util.Patterns
import androidx.preference.PreferenceManager
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import xyz.orangej.acmsigninsystemandroid.di.RetrofitApiModule.getServerRoot

/**
 * 通用的MIME-Type。
 */
private val mediaType: MediaType
    get() = "application/x-www-form-urlencoded".toMediaType()

/**
 * 为Request添加Session。
 *
 * @return RequestBuilder用于链式调用。
 */
fun Request.Builder.addSession(session: String): Request.Builder =
    addHeader("Cookie", "sessionid=$session")

/**
 * 为Request添加User-Agent。
 *
 * @return RequestBuilder用于链式调用。
 */
fun Request.Builder.addUA(): Request.Builder =
    this.apply {
        removeHeader("User-Agent")
        addHeader("User-Agent", "android")
    }

/**
 * 进行登录请求。
 *
 * @param context 上下文，用于从设置中读取服务器地址。
 * @param userName 用户名。
 * @param password 密码。
 * @return 响应结果json字符串。
 */
fun OkHttpClient.callLogin(context: Context, userName: String, password: String): String? {
    val requestBody = "username=$userName&password=$password"
        .toRequestBody(mediaType)
    val request = Request.Builder()
        .url(loginURL(context.getServerRoot()))
        .method("POST", requestBody)
        .addUA()
        .build()

    val call = this.newCall(request)
    val response = call.execute()
    return response.body?.string()
}

/**
 * 进行登出请求。
 *
 * @param context 上下文，用于从设置中读取服务器地址。
 * @param session 用户的Session。
 * @return 响应结果json字符串。
 */
fun OkHttpClient.callLogout(context: Context, session: String): String? {
    val requestBody = "".toRequestBody(mediaType)
    val request = Request.Builder()
        .url(logoutURL(context.getServerRoot()))
        .addSession(session)
        .method("POST", requestBody)
        .build()

    val call = this.newCall(request)
    val response = call.execute()
    return response.body?.string()
}

/**
 * 请求用户数据。
 *
 * @param context 上下文，用于从设置中读取服务器地址。
 * @param session 用户的Session。
 * @return 响应结果json字符串。
 */
fun OkHttpClient.callGetUserInfo(context: Context, session: String): String? {
    val requestBody = "".toRequestBody(mediaType)
    val request = Request.Builder()
        .url(userInfoURL(context.getServerRoot()))
        .addSession(session)
        .method("POST", requestBody)
        .build()

    val call = this.newCall(request)
    val response = call.execute()
    return response.body?.string()
}

/**
 * 请求训练记录。
 *
 * @param context 上下文，用于从设置中读取服务器地址。
 * @param session 用户的Session。
 * @return 响应结果的json字符串。
 */
fun OkHttpClient.callGetTrainingHistory(context: Context, session: String, id: Int): String? {
    val requestBody = (if (id == 0) {
        ""
    } else {
        "id=$id"
    }).toRequestBody(mediaType)
    val request = Request.Builder()
        .url(trainingHistoryURL(context.getServerRoot()))
        .addSession(session)
        .method("POST", requestBody)
        .build()

    val call = this.newCall(request)
    val response = call.execute()
    return response.body?.string()
}

/**
 * 请求特定的训练记录。
 *
 * @param context 上下文，用于从设置中读取服务器地址。
 * @param session 用户的Session。
 * @param id 要请求的训练记录ID。
 * @return 响应结果的json字符串。
 */
fun OkHttpClient.callGetSpecificTrainingHistory(
    context: Context,
    session: String,
    id: Long
): String? {
    val requestBody = "id=$id".toRequestBody(mediaType)
    val request = Request.Builder()
        .url(specificTrainingHistoryURL(context.getServerRoot()))
        .addSession(session)
        .method("POST", requestBody)
        .build()

    val call = this.newCall(request)
    val response = call.execute()
    return response.body?.string()
}

/**
 * 签到。
 *
 * @param context 上下文，用于从设置中读取服务器地址。
 * @param session 用户的Session。
 * @param csrfToken 表单中crsf_token的值。
 * @param token 表单中token的值。
 * @param time 表单中time的值。
 * @return 响应结果的json字符串。
 */
fun OkHttpClient.callSignIn(
    context: Context,
    session: String,
    csrfToken: String,
    token: String,
    time: String
): String? {
    val requestBody = "csrf_token=$csrfToken&token=$token&time=$time"
        .toRequestBody(mediaType)
    val request = Request.Builder()
        .url(signInURL(context.getServerRoot()))
        .addSession(session)
        .method("POST", requestBody)
        .build()

    val call = this.newCall(request)
    val response = call.execute()
    return response.body?.string()
}

/**
 * 注册。
 *
 * @param context 上下文，用于从设置中读取服务器地址。
 * @param username 用户名。
 * @param password 密码。
 * @param name 真实姓名。
 * @param department 学院。
 * @param admin 是否注册管理员。
 * @param adminVerify 管理员邀请码。
 * @param email 邮箱。
 * @param emailVerify 邮箱验证码。
 * @return 响应结果的json字符串。
 */
fun OkHttpClient.callSignUp(
    context: Context,
    username: String,
    password: String,
    name: String,
    department: String,
    admin: Boolean,
    adminVerify: String,
    email: String,
    emailVerify: String
): String? {
    val requestBody =
        "username=$username&password=$password&name=$name&department=$department&admin=$admin&adminVerify=$adminVerify&email=$email&emailVerify=$emailVerify"
            .toRequestBody(mediaType)
    val request = Request.Builder()
        .url(registerURL(context.getServerRoot()))
        .method("POST", requestBody)
        .build()

    val call = this.newCall(request)
    val response = call.execute()
    return response.body?.string()
}

/**
 * 获取邮箱验证码的方法。
 *
 * @param context 上下文，用于从设置中读取服务器地址。
 * @param username 用户名。
 * @param email 邮箱。
 * @return 响应结果的json字符串。
 */
fun OkHttpClient.callGetEmailCode(
    context: Context,
    username: String,
    email: String
): String? {
    val requestBody = "username=$username&email=$email"
        .toRequestBody(mediaType)
    val request = Request.Builder()
        .url(getEmailCodeURL(context.getServerRoot()))
        .method("POST", requestBody)
        .build()

    val call = this.newCall(request)
    val response = call.execute()
    return response.body?.string()
}

/**
 * 检查Api的方法。
 *
 * @param serverRoot 服务器地址。
 * @return 响应结果的json字符串。
 */
fun OkHttpClient.callCheckApi(
    serverRoot: String
): String? {
    val requestBody = "".toRequestBody(mediaType)
    val request = Request.Builder()
        .url(checkApiURL(serverRoot))
        .method("POST", requestBody)
        .build()

    val call = this.newCall(request)
    val response = call.execute()
    return response.body?.string()
}