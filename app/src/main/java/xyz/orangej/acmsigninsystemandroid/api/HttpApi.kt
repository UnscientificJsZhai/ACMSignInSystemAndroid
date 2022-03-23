package xyz.orangej.acmsigninsystemandroid.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.Header
import retrofit2.http.POST
import xyz.orangej.acmsigninsystemandroid.util.formattedSession

/**
 * Retrofit API接口。
 *
 * 其中所有需要Session作为参数的方法，都需要格式化后再传入。格式化方法为[formattedSession]。
 *
 * @see formattedSession
 */
interface HttpApi {

    /**
     * 进行登录请求。
     *
     * @param username 用户名。
     * @param password 密码。
     * @return 响应结果。
     */
    @POST("api/login/")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<ResponseBody>

    /**
     * 进行登出请求。
     *
     * @param formattedSession 用户的Session。必须格式化后才能使用。
     * @return 响应结果。
     */
    @POST("api/logout")
    fun logout(@Header("Cookie") formattedSession: String): Response<ResponseBody>

    /**
     * 请求用户数据。
     *
     * @param formattedSession 用户的Session。必须格式化后才能使用。
     * @return 响应结果。
     */
    @POST("api/getuserinfo/")
    fun getUserInfo(@Header("Cookie") formattedSession: String): Response<ResponseBody>

    /**
     * 获取训练记录。
     */
    //TODO 确认接口信息
    @POST("api/getrecord/")
    fun getTrainHistory(
        @Header("Cookie") formattedSession: String,
        @Field("start") start: Int? = null,
        @Field("amount") amount: Int? = null,
        @Field("id") id: String? = null
    ): Response<ResponseBody>

    /**
     * 请求特定的训练记录。
     *
     * @param formattedSession 用户的Session。必须格式化后才能使用。
     * @param id 要重新获取的记录的ID。
     * @return 特定的训练记录。响应结果。
     */
    @POST("api/getspecificrecord/")
    fun getSpecificTrainingHistory(
        @Header("Cookie") formattedSession: String,
        @Field("id") id: Long
    ): Response<ResponseBody>

    /**
     * 签到。
     *
     * @param formattedSession 用户的Session。必须格式化后才能使用。
     * @param csrfToken 表单中crsf_token的值。
     * @param token 表单中token的值。
     * @param time 表单中time的值。
     * @return 响应结果。
     */
    @POST("api/signin/")
    fun signIn(
        @Header("Cookie") formattedSession: String,
        @Field("csrf_token") csrfToken: String,
        @Field("token") token: String,
        @Field("time") time: String
    ): Response<ResponseBody>

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
     * @return 响应结果。
     */
    @POST("api/register/")
    fun signUp(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("department") department: String,
        @Field("admin") admin: Boolean,
        @Field("adminVerify") adminVerify: String,
        @Field("email") email: String,
        @Field("emailVerify") emailVerify: String
    ): Response<ResponseBody>

    /**
     * 获取邮箱验证码的方法。
     *
     * @param username 用户名。
     * @param email 邮箱。
     * @return 响应结果的json字符串。
     */
    @POST("api/getemailcode/")
    fun getEmailCode(
        @Field("username") username: String,
        @Field("email") email: String
    ): Response<ResponseBody>
}