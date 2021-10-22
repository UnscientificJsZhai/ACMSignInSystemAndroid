package xyz.orangej.acmsigninsystemandroid.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import xyz.orangej.acmsigninsystemandroid.R
import xyz.orangej.acmsigninsystemandroid.SystemApplication
import xyz.orangej.acmsigninsystemandroid.databinding.ActivityMainBinding
import xyz.orangej.acmsigninsystemandroid.ui.ProgressDialog
import xyz.orangej.acmsigninsystemandroid.ui.login.LoginActivity
import xyz.orangej.acmsigninsystemandroid.ui.main.MainActivityViewModel.SignInResult.ErrorCode.*
import xyz.orangej.acmsigninsystemandroid.ui.main.MainActivityViewModel.SignInResult.SuccessType.*
import xyz.orangej.acmsigninsystemandroid.ui.main.fragments.home.HomeFragment
import java.net.SocketException
import javax.net.ssl.SSLHandshakeException

class MainActivity : AppCompatActivity() {

    private lateinit var systemApplication: SystemApplication

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.systemApplication = application as SystemApplication
        val dao = systemApplication.getDatabase().userDao()
        val hash = systemApplication.session.hashCode()
        this.viewModel = ViewModelProvider(
            this,
            MainActivityViewModel.Factory(dao, hash)
        )[MainActivityViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel.viewModelScope.launch {
            val result = viewModel.getCurrentUser(systemApplication.session)

            if (result is MainActivityViewModel.GetUserResult.Error) {
                if (result.exception is SocketException || result.exception is SSLHandshakeException) {
                    Toast.makeText(this@MainActivity, "网络异常", Toast.LENGTH_SHORT).show()
                    return@launch
                } else if (result.exception is AssertionError) {
                    Toast.makeText(this@MainActivity, "登录过期", Toast.LENGTH_SHORT).show()
                    systemApplication.session = ""
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
            } else if (result is MainActivityViewModel.GetUserResult.Success) {
                withContext(Dispatchers.IO) {
                    dao.addCurrentUser(result.data)
                }
            }
        }
    }

    @Suppress("deprecation")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == HomeFragment.REQUEST_CODE_SCAN_ONE) {
            if (resultCode == RESULT_OK) {
                val obj = data?.getParcelableExtra(ScanUtil.RESULT) as HmsScan?
                processSignIn(obj)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /**
     * 处理登录的逻辑。
     *
     * @param data HMS扫码取得的结果。
     */
    private fun processSignIn(data: HmsScan?) {
        val signInData = qrCodeToSignInInfo(data?.originalValue ?: "")
        if (signInData == null) {
            Toast.makeText(this, R.string.home_invalidQRCode, Toast.LENGTH_SHORT).show()
        } else {
            viewModel.viewModelScope.launch {
                val dialog = ProgressDialog(this@MainActivity)
                dialog.show()
                val response = viewModel.signIn(
                    csrfToken = signInData.csrfToken,
                    token = signInData.token,
                    time = signInData.time,
                    session = systemApplication.session
                )
                when (response) {
                    is MainActivityViewModel.SignInResult.Error -> {
                        val errorMessage = when (response.errorCode) {
                            NETWORK_ERROR -> getString(R.string.signIn_error_network)
                            ILLEGAL_DATA -> getString(R.string.signIn_error_illegal)
                            FAIL_TO_SIGN_IN -> getString(R.string.signIn_error_fail)
                        }
                        Toast.makeText(
                            this@MainActivity,
                            "${getString(R.string.signIn_error)} $errorMessage",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is MainActivityViewModel.SignInResult.Success -> {
                        Toast.makeText(
                            this@MainActivity,
                            when (response.message) {
                                START -> getString(R.string.signIn_success_1)
                                END -> getString(R.string.signIn_success_2)
                            },
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                dialog.postDismiss()
            }
        }
    }

    /**
     * 签到信息数据类。
     */
    private data class SignInData(
        val csrfToken: String,
        val token: String,
        val time: String
    )

    /**
     * 将二维码转换为签到信息。
     *
     * @param originalValue 从二维码中获取的信息。
     * @return 生成的签到信息的数据类。
     */
    private fun qrCodeToSignInInfo(originalValue: String): SignInData? {
        val jsonObject = try {
            JSONObject(originalValue)
        } catch (e: JSONException) {
            return null
        }
        return try {
            SignInData(
                csrfToken = jsonObject.getString("csrf_token"),
                token = jsonObject.getString("token"),
                time = jsonObject.getString("time")
            )
        } catch (e: JSONException) {
            null
        }
    }
}