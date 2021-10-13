package xyz.orangej.acmsigninsystemandroid.ui.login

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignUpPage()
        }
    }

    @Composable
    fun SignUpPage() {
        Text(text = "Hello World")
    }

    @Preview
    @Composable
    fun SignUpPagePreview() {
        SignUpPage()
    }
}