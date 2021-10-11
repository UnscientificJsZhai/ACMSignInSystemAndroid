package xyz.orangej.acmsigninsystemandroid

import okhttp3.OkHttpClient
import org.json.JSONObject
import org.junit.Test
import xyz.orangej.acmsigninsystemandroid.api.callGetUserInfo
import xyz.orangej.acmsigninsystemandroid.api.callLogin

class LoginTest {

    @Test
    fun loginTest() {
        val client = OkHttpClient()
        val jsonString = client.callLogin("UserFragment", "UserFragment")
        println(jsonString)
        assert(true)
    }

    @Test
    fun userInfoTest() {
        val client = OkHttpClient()
        val jsonString = client.callGetUserInfo("scdndcbwwuovh1kt1ehs3h7z0wy1b28v")
        val objects = JSONObject(jsonString)
        println(objects)
        assert(true)
    }
}