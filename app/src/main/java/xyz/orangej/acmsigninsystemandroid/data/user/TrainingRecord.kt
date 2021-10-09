package xyz.orangej.acmsigninsystemandroid.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 训练时间的数据类。
 *
 * @param sessionHash 用户Session的哈希校验。
 * @param id 服务器端数据库的训练记录ID。
 * @param userName 用户登录名。
 * @param startTime 训练开始时间。
 * @param endTime 训练结束时间。
 * @param valid 训练记录是否有效。
 * @param isRecord 训练记录是否被记录。
 * @param timeLength 训练时间长度。
 */
@Entity(tableName = TrainingRecord.TABLE_NAME)
data class TrainingRecord(
    @PrimaryKey val sessionHash: Int,
    val id: String,
    val userName: String,
    val startTime: String,
    val endTime: String,
    val valid: Boolean,
    val isRecord: Boolean,
    val timeLength: String
) {

    companion object {

        const val TABLE_NAME = "training_record"
    }
}