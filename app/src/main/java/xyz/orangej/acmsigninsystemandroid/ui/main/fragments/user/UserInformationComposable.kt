package xyz.orangej.acmsigninsystemandroid.ui.main.fragments.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import xyz.orangej.acmsigninsystemandroid.R
import xyz.orangej.acmsigninsystemandroid.data.user.CurrentUser
import xyz.orangej.acmsigninsystemandroid.ui.main.fragments.dashboard.mainTextColor

/**
 * 用户信息页。
 *
 * @param data 用户数据。
 * @param onLogout 按下退出按键时的操作。
 */
@Composable
fun InformationPage(data: CurrentUser, onLogout: () -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val (userName, informationBox, logoutButton) = createRefs()

        Text(
            text = data.displayName,
            fontSize = 36.sp,
            modifier = Modifier.constrainAs(userName) {
                end.linkTo(parent.end, 16.dp)
                top.linkTo(parent.top, 8.dp)
            },
            color = mainTextColor()
        )

        Column(
            modifier = Modifier
                .constrainAs(informationBox) {
                    top.linkTo(userName.bottom, 16.dp)
                }
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.userInformation_email) + " " + data.email,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.userInformation_department) + " " + data.department,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.userInformation_major) + " " + data.major,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.userInformation_joinTime) + " " + data.joinTime,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.userInformation_allTrainingTime) + " " + data.allTrainingTime,
                color = Color.Gray
            )
        }

        MaterialTheme {
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .width(120.dp)
                    .wrapContentHeight()
                    .constrainAs(logoutButton) {
                        top.linkTo(informationBox.bottom, 64.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Text(text = stringResource(R.string.settings_logout))
            }
        }
    }
}

/**
 * 显示用户信息的页面。可以滚动。
 *
 * @param data 用户数据。
 * @param onLogout 按下退出按键时的操作。
 */
@Composable
fun UserPage(data: CurrentUser, onLogout: () -> Unit) {
    LazyColumn {
        item {
            InformationPage(data, onLogout)
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
