package xyz.orangej.acmsigninsystemandroid.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import xyz.orangej.acmsigninsystemandroid.R
import xyz.orangej.acmsigninsystemandroid.ui.main.fragments.dashboard.mainTextColor

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InfoActivityMainPage(::onButtonClick)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = if (item.itemId == android.R.id.home) {
        finish()
        true
    } else super.onOptionsItemSelected(item)


    private fun onButtonClick() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.github.com/UnscientificJsZhai/ACMSignInSystemAndroid")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    @Composable
    fun InfoActivityMainPage(onButtonClick: () -> Unit) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (icon, title, developer, information, github) = createRefs()

            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "icon",
                modifier = Modifier
                    .constrainAs(icon) {
                        top.linkTo(parent.top, 32.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .size(128.dp)
            )

            Text(
                text = stringResource(R.string.app_name),
                color = mainTextColor(),
                fontSize = 36.sp,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(icon.bottom, 32.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            Text(
                text = stringResource(R.string.info_sub),
                fontSize = 24.sp,
                color = if (isSystemInDarkTheme()) {
                    Color.LightGray
                } else {
                    Color.DarkGray
                },
                modifier = Modifier.constrainAs(developer) {
                    top.linkTo(title.bottom, 24.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            Column(
                modifier = Modifier
                    .constrainAs(information) {
                        top.linkTo(developer.bottom, 24.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(12.dp)
            ) {

            }

            MaterialTheme {
                Button(onClick = onButtonClick, modifier = Modifier.constrainAs(github) {
                    top.linkTo(information.bottom, 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                    Text("Github")
                }
            }
        }
    }

    @Preview
    @Composable
    fun InfoPreview() {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            InfoActivityMainPage {}
        }
    }
}