package xyz.orangej.acmsigninsystemandroid.api

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import xyz.orangej.acmsigninsystemandroid.di.RetrofitApiModule

const val MEDIA_TYPE = "application/x-www-form-urlencoded"

/**
 * 测试API可用性。
 *
 * @param serverRoot 服务器域名。
 * @return 测试响应内容。
 */
fun OkHttpClient.callCheckApi(serverRoot: String): String? {
    val requestBody = "".toRequestBody(MEDIA_TYPE.toMediaType())
    val request = Request.Builder()
        .url(RetrofitApiModule.formatServerAddress(serverRoot) + "/api/")
        .method("POST", requestBody)
        .build()

    val call = this.newCall(request)
    val response = call.execute()
    return response.body?.string()
}