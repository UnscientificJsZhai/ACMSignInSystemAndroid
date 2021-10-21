package xyz.orangej.acmsigninsystemandroid.ui.login.sign

import android.os.CountDownTimer
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject
import xyz.orangej.acmsigninsystemandroid.api.callGetEmailCode
import xyz.orangej.acmsigninsystemandroid.api.callSignUp
import xyz.orangej.acmsigninsystemandroid.ui.login.LoginViewModel
import java.io.IOException

class SignUpActivityViewModel : ViewModel() {

    private val client = OkHttpClient()

    val userName = MutableLiveData("")
    val password = MutableLiveData("")
    val passwordConfirm = MutableLiveData("")
    val name = MutableLiveData("")
    val department = MutableLiveData("")
    val admin = MutableLiveData(false)
    val adminVerify = MutableLiveData("")
    val email = MutableLiveData("")
    val emailVerify = MutableLiveData("")

    private val timeout = MutableLiveData(0)
    val resendTimeout get() = timeout as LiveData<Int>

    /**
     * 为邮箱验证码获取按钮设定可按倒计时。
     *
     * @param second 倒计时时间，秒。
     */
    fun setTime(second: Int = 60) {
        val timer = object : CountDownTimer((second * 1000).toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timeout.value = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {}
        }
        timer.start()
    }

    /**
     * 检查当前输入数据是否合法。
     *
     * @return 如果合法返回true，否则返回false。
     */
    fun isDataLegal(): Boolean {
        //用户名无效
        if (!LoginViewModel.isUserNameValid(username = this.userName.value ?: return false)) {
            return false
        }

        //密码和确认密码不相符
        if (this.password.value != this.passwordConfirm.value ?: return false) {
            return false
        }

        //密码不符合规则
        if (!LoginViewModel.isPasswordValid(password = this.password.value ?: return false)) {
            return false
        }

        //注册管理员但不填写管理员邀请码
        if ((this.admin.value ?: return false) && (adminVerify.value ?: return false) == "") {
            return false
        }

        //邮箱地址不合法
        if (!Patterns.EMAIL_ADDRESS.matcher(this.email.value ?: return false).matches()) {
            return false
        }

        //不填写邮箱验证码
        if ((this.emailVerify.value ?: return false) == "") {
            return false
        }

        return true
    }

    /**
     * 注册请求结果枚举。
     */
    enum class SignUpResult {

        ERROR, NETWORK_ERROR, ADMIN_VERIFY_FAILED, SUCCESS
    }

    /**
     * 发起注册请求。
     */
    suspend fun signUp(): SignUpResult {
        val username = this.userName.value ?: ""
        val password = this.password.value ?: ""
        val name = this.name.value ?: ""
        val department = this.department.value ?: ""
        val admin = this.admin.value ?: false
        val adminVerify = this.adminVerify.value ?: ""
        val email = this.email.value ?: ""
        val emailVerify = this.emailVerify.value ?: ""

        val response = try {
            withContext(Dispatchers.IO) {
                this@SignUpActivityViewModel.client.callSignUp(
                    username,
                    password,
                    name,
                    department,
                    admin,
                    adminVerify,
                    email,
                    emailVerify
                )
            }
        } catch (e: IOException) {
            return SignUpResult.NETWORK_ERROR
        }
        val json = try {
            JSONObject(response ?: return SignUpResult.NETWORK_ERROR)
        } catch (e: JSONException) {
            return SignUpResult.ERROR
        }
        try {
            val status = json.getString("status")
            if (status != "success") {
                return if (json.getString("msg").contains("管理员验证码")) {
                    SignUpResult.ADMIN_VERIFY_FAILED
                } else {
                    SignUpResult.ERROR
                }
            }
            val dataJsonObject = json.getJSONObject("data")
            dataJsonObject.getInt("userId")
            return SignUpResult.SUCCESS
        } catch (e: JSONException) {
            return SignUpResult.ERROR
        }
    }

    /**
     * 获取邮箱验证码。调用前需要确认用户名和邮箱地址合法。
     */
    suspend fun getEmailVerifyCode(): Boolean {
        val username = this.userName.value ?: ""
        val email = this.email.value ?: ""

        val response = try {
            withContext(Dispatchers.IO) {
                this@SignUpActivityViewModel.client.callGetEmailCode(username, email)
            }
        } catch (e: IOException) {
            return false
        }

        return response?.contains("success") == true
    }

}