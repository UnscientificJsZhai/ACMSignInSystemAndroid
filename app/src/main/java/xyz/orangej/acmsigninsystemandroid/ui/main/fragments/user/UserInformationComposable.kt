package xyz.orangej.acmsigninsystemandroid.ui.main.fragments.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.orangej.acmsigninsystemandroid.data.user.CurrentUser

@Composable
fun InformationPage(data: CurrentUser) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color.White)
            .padding(12.dp)
    ) {
        Text(
            text = data.displayName,
            fontSize = 36.sp,
            modifier = Modifier.clickable {

            }
        )
    }
}

@Preview
@Composable
fun InformationPreview() {
    val data by remember {
        mutableStateOf(
            CurrentUser(
                sessionHash = 0,
                userName = "user",
                displayName = "用户",
                email = "user@mail.nwpu.edu.cn",
                department = "A部门",
                major = "软件工程",
                joinTime = "2021.10.10",
                allTrainingTime = "0天0小时0分钟40秒"
            )
        )
    }

    InformationPage(data)
}
