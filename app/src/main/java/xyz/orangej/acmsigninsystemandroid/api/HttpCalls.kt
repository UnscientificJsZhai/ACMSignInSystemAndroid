@file:JvmName("HttpCalls")

package xyz.orangej.acmsigninsystemandroid.api

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

/**
 * 通用的MIME-Type。
 */
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

/**
 * 请求用户数据。
 *
 * @param session 用户的Session。
 * @return 响应结果json字符串。
 */
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

/**
 * 请求训练记录。
 *
 * @param session 用户的Session。
 * @return 响应结果的json字符串。
 */
fun OkHttpClient.callGetTrainingHistory(session: String, id: Int): String? {
    val requestBody = RequestBody.create(
        mediaType,
        if (id == 0) {
            ""
        } else {
            "id=$id"
        }
    )
    val request = Request.Builder()
        .url(trainingHistoryURL())
        .addSession(session)
        .method("POST", requestBody)
        .build()

    val call = this.newCall(request)
    val response = call.execute()
    return response.body()?.string()
}

/**
 * 请求特定的训练记录。
 *
 * @param session 用户的Session。
 * @param id 要请求的训练记录ID。
 * @return 响应结果的json字符串。
 */
fun OkHttpClient.callGetSpecificTrainingHistory(session: String, id: Long): String? {
    val requestBody = RequestBody.create(
        mediaType,
        "id=$id"
    )
    val request = Request.Builder()
        .url(specificTrainingHistoryURL())
        .addSession(session)
        .method("POST", requestBody)
        .build()

    val call = this.newCall(request)
    val response = call.execute()
    return response.body()?.string()
}

/**
 * 签到。
 *
 * @param session 用户的Session。
 * @param csrfToken 表单中crsf_token的值。
 * @param token 表单中token的值。
 * @param time 表单中time的值。
 * @return 响应结果的json字符串。
 */
fun OkHttpClient.callSignIn(
    session: String,
    csrfToken: String,
    token: String,
    time: String
): String? {
    val requestBody = RequestBody.create(
        mediaType,
        "csrf_token=$csrfToken&token=$token&time=$time"
    )
    val request = Request.Builder()
        .url(signInURL())
        .addSession(session)
        .method("POST", requestBody)
        .build()

    val call = this.newCall(request)
    val response = call.execute()
    return response.body()?.string()
}

/**
 * 注册。
 *
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
    username: String,
    password: String,
    name: String,
    department: String,
    admin: Boolean,
    adminVerify: String,
    email: String,
    emailVerify: String
): String? {
    val requestBody = RequestBody.create(
        mediaType,
        "username=$username&password=$password&name=$name&department=$department&admin=$admin&adminVerify=$adminVerify&email=$email&emailVerify=$emailVerify"
    )
    val request = Request.Builder()
        .url(registerURL())
        .method("POST", requestBody)
        .build()

    val call = this.newCall(request)
    val response = call.execute()
    return response.body()?.string()
}

/**
 * 获取邮箱验证码的方法。
 *
 * @param username 用户名。
 * @param email 邮箱。
 * @return 响应结果的json字符串。
 */
fun OkHttpClient.callGetEmailCode(
    username: String,
    email: String
): String? {
    val requestBody = RequestBody.create(
        mediaType,
        "username=$username&email=$email"
    )
    val request = Request.Builder()
        .url(getEmailCodeURL())
        .method("POST", requestBody)
        .build()

    val call = this.newCall(request)
    val response = call.execute()
    return response.body()?.string()
}