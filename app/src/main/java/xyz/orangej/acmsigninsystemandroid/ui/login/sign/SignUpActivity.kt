package xyz.orangej.acmsigninsystemandroid.ui.login.sign

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import xyz.orangej.acmsigninsystemandroid.R
import xyz.orangej.acmsigninsystemandroid.ui.login.LoginActivity
import xyz.orangej.acmsigninsystemandroid.ui.login.LoginViewModel
import xyz.orangej.acmsigninsystemandroid.ui.main.fragments.dashboard.mainTextColor

class SignUpActivity : AppCompatActivity() {

    private lateinit var viewModel: SignUpActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.viewModel = ViewModelProvider(this)[SignUpActivityViewModel::class.java]

        setContent {
            SignUpPage(onButtonClick = ::onSignUpButtonClick)
        }
    }

    /**
     * 按下注册按钮时的响应。
     */
    private fun onSignUpButtonClick() {
        if (viewModel.isDataLegal()) {
            viewModel.viewModelScope.launch {
                when (viewModel.signUp()) {
                    SignUpActivityViewModel.SignUpResult.SUCCESS -> onSuccessSignUp()
                    SignUpActivityViewModel.SignUpResult.ERROR -> Toast.makeText(
                        this@SignUpActivity,
                        R.string.signUp_error,
                        Toast.LENGTH_SHORT
                    ).show()
                    SignUpActivityViewModel.SignUpResult.ADMIN_VERIFY_FAILED -> Toast.makeText(
                        this@SignUpActivity,
                        R.string.signUp_adminVerifyError,
                        Toast.LENGTH_SHORT
                    ).show()
                    SignUpActivityViewModel.SignUpResult.NETWORK_ERROR -> Toast.makeText(
                        this@SignUpActivity,
                        R.string.signIn_error_network,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(this, "不合法", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 注册成功时执行的方法。
     */
    private fun onSuccessSignUp() {
        Toast.makeText(this, R.string.signUp_success, Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    /**
     * 按下发送验证码按钮时的响应。
     */
    private fun onSendEmailVerifyCodeButtonClick() {
        if (LoginViewModel.isUserNameValid(viewModel.userName.value ?: "")) {
            if (Patterns.EMAIL_ADDRESS.matcher(viewModel.email.value ?: "").matches()) {
                viewModel.viewModelScope.launch {
                    if (viewModel.getEmailVerifyCode()) {
                        viewModel.setTime()
                    } else {
                        Toast.makeText(
                            this@SignUpActivity,
                            R.string.signUp_emailVerifyCodeError,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                return
            }
        }
        Toast.makeText(
            this,
            R.string.signUp_emailNotLegal,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }

    /**
     * 登录页面。
     *
     * @param onButtonClick 按下注册按钮时的回调。
     */
    @Composable
    fun SignUpPage(viewModel: SignUpActivityViewModel = viewModel(), onButtonClick: () -> Unit) {

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (inputArea, button) = createRefs()

            LazyColumn(modifier = Modifier
                .constrainAs(inputArea) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(button.top)
                    height = Dimension.fillToConstraints
                }
                .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)) {
                item {
                    InputArea(viewModel)
                }
            }

            MaterialTheme {
                Button(onClick = onButtonClick, modifier = Modifier.constrainAs(button) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, 32.dp)
                    top.linkTo(inputArea.bottom)
                }) {
                    Text(text = stringResource(R.string.sign_up_noLine))
                }
            }
        }
    }

    /**
     * 输入区域。
     */
    @Composable
    fun InputArea(viewModel: SignUpActivityViewModel) {
        val userName by viewModel.userName.observeAsState("")
        val password by viewModel.password.observeAsState("")
        val passwordConfirm by viewModel.passwordConfirm.observeAsState("")
        val name by viewModel.name.observeAsState("")
        val department by viewModel.department.observeAsState("")
        val admin by viewModel.admin.observeAsState(false)
        val adminVerify by viewModel.adminVerify.observeAsState("")
        val email by viewModel.email.observeAsState("")
        val emailVerify by viewModel.emailVerify.observeAsState("")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            MaterialTheme {
                Text(
                    text = stringResource(R.string.sign_up_title),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 12.dp),
                    color = mainTextColor(),
                    fontSize = 36.sp
                )

                Spacer(modifier = Modifier.height(48.dp))

                var isUserNameValid by remember {
                    mutableStateOf(true)
                }
                OutlinedTextField(
                    value = userName,
                    onValueChange = {
                        viewModel.userName.value = it
                        isUserNameValid = LoginViewModel.isUserNameValid(it)
                    },
                    label = {
                        Text(stringResource(R.string.signUp_username))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isUserNameValid,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                var isPasswordValid by remember {
                    mutableStateOf(true)
                }
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        viewModel.password.value = it
                        isPasswordValid = LoginViewModel.isPasswordValid(it)
                    },
                    label = {
                        Text(stringResource(R.string.signUp_password))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isPasswordValid,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                )


                Spacer(modifier = Modifier.height(12.dp))

                var isPasswordConfirmed by remember {
                    mutableStateOf(true)
                }
                OutlinedTextField(
                    value = passwordConfirm,
                    onValueChange = {
                        viewModel.passwordConfirm.value = it
                        isPasswordConfirmed = it == password && isPasswordValid
                    },
                    label = {
                        Text(stringResource(R.string.signUp_passwordConfirm))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isPasswordConfirmed,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        viewModel.name.value = it
                    },
                    label = {
                        Text(stringResource(R.string.signUp_name))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = department,
                    onValueChange = {
                        viewModel.department.value = it
                    },
                    label = {
                        Text(stringResource(R.string.signUp_department))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                var isEmailValid by remember {
                    mutableStateOf(true)
                }
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        viewModel.email.value = it
                        isEmailValid = Patterns.EMAIL_ADDRESS.matcher(it).matches()
                    },
                    label = {
                        Text(stringResource(R.string.signUp_email))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    ),
                    isError = !isEmailValid
                )

                Spacer(modifier = Modifier.height(12.dp))

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    val (textField, button) = createRefs()

                    OutlinedTextField(
                        value = emailVerify,
                        onValueChange = {
                            viewModel.emailVerify.value = it
                        },
                        label = {
                            Text(stringResource(R.string.signUp_emailVerify))
                        },
                        modifier = Modifier.constrainAs(textField) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(button.start, 8.dp)
                            width = Dimension.fillToConstraints
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )

                    val resendTimeout by viewModel.resendTimeout.observeAsState(0)
                    Button(
                        onClick = {
                            onSendEmailVerifyCodeButtonClick()
                            viewModel.setTime()
                        }, modifier = Modifier.constrainAs(button) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }, enabled = resendTimeout == 0
                    ) {
                        Text(
                            text = if (resendTimeout == 0) {
                                stringResource(R.string.signUp_resend)
                            } else {
                                stringResource(R.string.signUp_resendTimeout).format(resendTimeout)
                            }, maxLines = 1, modifier = Modifier.wrapContentWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row {
                    Checkbox(checked = admin, onCheckedChange = {
                        viewModel.admin.value = it
                    })

                    Text(
                        text = stringResource(R.string.signUp_admin),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                if (admin) {
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = adminVerify,
                        onValueChange = {
                            viewModel.adminVerify.value = it
                        },
                        label = {
                            Text(stringResource(R.string.signUp_adminVerify))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )
                }
            }
        }
    }

    @Preview
    @Composable
    fun SignUpPagePreview() {
        Column(modifier = Modifier.background(Color.White)) {
            SignUpPage(viewModel = SignUpActivityViewModel()) {}
        }
    }

    @Preview
    @Composable
    fun Overall() {
        Column(modifier = Modifier.background(Color.White)) {
            InputArea(viewModel = SignUpActivityViewModel())
        }
    }
}