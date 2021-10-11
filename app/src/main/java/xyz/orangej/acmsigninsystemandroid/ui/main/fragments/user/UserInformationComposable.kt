package xyz.orangej.acmsigninsystemandroid.ui.main.fragments.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
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
import androidx.constraintlayout.compose.ConstraintLayout
import xyz.orangej.acmsigninsystemandroid.data.user.CurrentUser

@Composable
fun InformationPage(data: CurrentUser, onLogout: () -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color.White)
    ) {
        val (userName, informationBox, logoutButton) = createRefs()

        Text(
            text = data.displayName,
            fontSize = 36.sp,
            modifier = Modifier.constrainAs(userName) {
                end.linkTo(parent.end, 16.dp)
            }
        )

        Column(
            modifier = Modifier
                .constrainAs(informationBox) {
                    top.linkTo(userName.bottom, 12.dp)
                }
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = "邮箱:${data.email}",
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "部门:${data.department}",
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "专业:${data.major}",
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "加入时间:${data.joinTime}",
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "总训练时间:${data.allTrainingTime}",
                color = Color.Gray
            )
        }

        MaterialTheme {
            Button(
                onClick = onLogout,
                modifier = Modifier.constrainAs(logoutButton) {
                    top.linkTo(informationBox.bottom, 32.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) {
                Text(text = "登出")
            }
        }
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

    InformationPage(data) {}
}
