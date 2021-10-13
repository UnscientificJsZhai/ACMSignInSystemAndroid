package xyz.orangej.acmsigninsystemandroid.ui.main.fragments.dashboard

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import xyz.orangej.acmsigninsystemandroid.R
import xyz.orangej.acmsigninsystemandroid.data.user.TrainingRecord

/**
 * 强调文字的颜色。根据系统深色模式配置更改。
 *
 * @return 用于Compose的Color。
 */
@Composable
fun mainTextColor() = if (isSystemInDarkTheme()) {
    Color.White
} else {
    Color.Black
}

/**
 * 显示单个训练记录的卡片。
 *
 * @param data 训练记录数据类对象。
 */
@Composable
fun TrainingHistoryCard(data: TrainingRecord) {
    var expanded by remember {
        mutableStateOf(false)
    }

    MaterialTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable {
                    expanded = !expanded
                }
                .animateContentSize()
                .padding(12.dp),
            elevation = 4.dp,
            shape = RoundedCornerShape(12.dp),
            backgroundColor = if (isSystemInDarkTheme()) {
                Color.DarkGray
            } else {
                Color.White
            }
        ) {
            ConstraintLayout {
                val (id, status, trainingHistoryTitle, record, startTime, endTime) = createRefs()

                Text(
                    text = stringResource(R.string.trainingRecord_id) + data.id,
                    modifier = Modifier.constrainAs(id) {
                        top.linkTo(parent.top, 8.dp)
                        start.linkTo(parent.start, 16.dp)
                    },
                    fontSize = 12.sp,
                    color = Color.Gray,
                )

                val statusModifier = Modifier.constrainAs(status) {
                    end.linkTo(parent.end, 12.dp)
                    top.linkTo(parent.top, 8.dp)
                }
                when (data.status) {
                    TrainingRecord.STATUS_UNFINISHED -> Text(
                        text = stringResource(R.string.trainingRecord_status_0),
                        modifier = statusModifier,
                        color = Color.Red
                    )
                    TrainingRecord.STATUS_NOT_VALID -> Text(
                        text = stringResource(R.string.trainingRecord_status_1),
                        modifier = statusModifier,
                        color = Color.Red
                    )
                    TrainingRecord.STATUS_NOT_RECORDED -> Text(
                        text = stringResource(R.string.trainingRecord_status_2),
                        modifier = statusModifier,
                        color = Color(0xFFFFA500)
                    )
                    TrainingRecord.STATUS_RECORDED -> Text(
                        text = stringResource(R.string.trainingRecord_status_3),
                        modifier = statusModifier,
                        color = mainTextColor()
                    )
                }

                Text(
                    text = stringResource(R.string.trainingRecord_length),
                    modifier = Modifier.constrainAs(trainingHistoryTitle) {
                        top.linkTo(id.bottom, 16.dp)
                        start.linkTo(parent.start, 16.dp)
                    },
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = data.timeLength,
                    modifier = Modifier.constrainAs(record) {
                        top.linkTo(trainingHistoryTitle.bottom, 4.dp)
                        start.linkTo(trainingHistoryTitle.start, 0.dp)
                        if (!expanded) {
                            bottom.linkTo(parent.bottom, 12.dp)
                        }
                    },
                    fontSize = 26.sp,
                    color = mainTextColor()
                )

                if (expanded) {
                    Text(
                        text = stringResource(R.string.trainingRecord_start) + data.startTime,
                        modifier = Modifier.constrainAs(startTime) {
                            top.linkTo(record.bottom, 16.dp)
                            start.linkTo(parent.start, 16.dp)
                        },
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = stringResource(R.string.trainingRecord_end) + data.endTime,
                        modifier = Modifier.constrainAs(endTime) {
                            top.linkTo(startTime.bottom, 8.dp)
                            start.linkTo(startTime.start, 0.dp)
                            bottom.linkTo(parent.bottom, 8.dp)
                        },
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun RecordRecyclerPreview() {
    val data1 by remember {
        mutableStateOf(
            TrainingRecord(
                sessionHash = 0,
                id = 123,
                userName = "user",
                startTime = "2021.09.22 11:10:00",
                endTime = "2021.09.22 11:10:30",
                status = TrainingRecord.STATUS_RECORDED,
                timeLength = "0天0小时0分钟20秒"
            )
        )
    }
    val data2 by remember {
        mutableStateOf(
            TrainingRecord(
                sessionHash = 0,
                id = 123,
                userName = "user",
                startTime = "2021.09.22 11:10:00",
                endTime = "2021.09.22 11:10:30",
                status = 0,
                timeLength = "0天0小时0分钟20秒"
            )
        )
    }
    val data3 by remember {
        mutableStateOf(
            TrainingRecord(
                sessionHash = 0,
                id = 123,
                userName = "user",
                startTime = "2021.09.22 11:10:00",
                endTime = "2021.09.22 11:10:30",
                status = 1,
                timeLength = "0天0小时0分钟20秒"
            )
        )
    }
    val data4 by remember {
        mutableStateOf(
            TrainingRecord(
                sessionHash = 0,
                id = 123,
                userName = "user",
                startTime = "2021.09.22 11:10:00",
                endTime = "2021.09.22 11:10:30",
                status = 2,
                timeLength = "0天0小时0分钟20秒"
            )
        )
    }

    Column(
        Modifier
            .background(color = Color.White)
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        TrainingHistoryCard(data = data1)
        TrainingHistoryCard(data = data2)
        TrainingHistoryCard(data = data3)
        TrainingHistoryCard(data = data4)
    }
}