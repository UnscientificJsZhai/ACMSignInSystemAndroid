package xyz.orangej.acmsigninsystemandroid.ui.main.fragments.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.constraintlayout.compose.ConstraintLayout
import xyz.orangej.acmsigninsystemandroid.R

/**
 * 主页。
 *
 * @param onButtonClick 按下按钮时的回调函数。
 */
@Composable
fun HomePage(onButtonClick: () -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val (image, title) = createRefs()

        Image(
            painter = painterResource(R.drawable.ic_home_black_24dp),
            contentDescription = "",
            modifier = Modifier
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, 120.dp)
                }
                .height(120.dp)
                .width(120.dp)
        )

        MaterialTheme {
            Button(onClick = onButtonClick, modifier = Modifier.constrainAs(title) {
                top.linkTo(image.bottom, 64.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
                Text(text = stringResource(R.string.home_scanButton))
            }
        }
    }
}

@Preview
@Composable
fun HomePagePreview() {
    Column(modifier = Modifier.background(Color.White)) {
        HomePage {}
    }
}