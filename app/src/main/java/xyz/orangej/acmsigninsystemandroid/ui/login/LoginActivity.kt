package xyz.orangej.acmsigninsystemandroid.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.orangej.acmsigninsystemandroid.R
import xyz.orangej.acmsigninsystemandroid.SystemApplication
import xyz.orangej.acmsigninsystemandroid.databinding.ActivityLoginBinding
import xyz.orangej.acmsigninsystemandroid.ui.login.sign.SignUpActivity
import xyz.orangej.acmsigninsystemandroid.ui.main.MainActivity
import xyz.orangej.acmsigninsystemandroid.ui.settings.SettingsActivity

/**
 * 登录Activity。
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var systemApplication: SystemApplication

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.systemApplication = application as SystemApplication
        if (systemApplication.session.isNotBlank()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading

        val loginRepository = this.systemApplication.loginRepository
        loginViewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(loginRepository)
        )[LoginViewModel::class.java]

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            //用户名密码不合法时禁用登录按钮
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
                login.isEnabled = true
            }
            if (loginResult.success != null) {
                showLoginToast(loginResult.success)
                setResult(Activity.RESULT_OK)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        login.callOnClick()
                }
                false
            }

            login.setOnClickListener {
                login.isEnabled = false
                loginViewModel.viewModelScope.launch {
                    loading.visibility = View.VISIBLE
                    systemApplication.session = //登录成功即保存Session
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            val dao = systemApplication.getDatabase().userDao()
            withContext(Dispatchers.IO) {
                dao.deleteUserCache()
            }
        }

        binding.signUp?.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * 显示登录成功。
     *
     * @param model 登录成功的用户。
     */
    private fun showLoginToast(model: LoginResult.LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.login_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 显示登录失败的Toast弹窗。
     *
     * @param errorString 失败提醒的字符串资源。
     */
    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    /**
     * 简化为EditText设置afterTextChanged监听的扩展函数。
     *
     * @param afterTextChanged 要注册的afterTextChanged逻辑。
     */
    private inline fun EditText.afterTextChanged(crossinline afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}