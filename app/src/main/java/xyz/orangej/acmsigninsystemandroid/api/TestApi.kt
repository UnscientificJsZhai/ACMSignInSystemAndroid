package xyz.orangej.acmsigninsystemandroid.api

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import xyz.orangej.acmsigninsystemandroid.di.RetrofitApiModule

const val MEDIA_TYPE = "application/x-www-form-urlencoded"

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