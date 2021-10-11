package xyz.orangej.acmsigninsystemandroid.data.user

import androidx.annotation.IntRange
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 训练时间的数据类。
 *
 * @param sessionHash 用户Session的哈希校验。
 * @param id 服务器端数据库的训练记录ID。
 * @param userName 用户登录名。
 * @param startTime 训练开始时间。
 * @param endTime 训练结束时间。
 * @param status 训练记录状态。
 * @param timeLength 训练时间长度。
 */
@Entity(
    tableName = TrainingRecord.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = CurrentUser::class,
        parentColumns = ["sessionHash"],
        childColumns = ["sessionHash"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["sessionHash"])]
)
data class TrainingRecord(
    val sessionHash: Int,
    @PrimaryKey val id: Int,
    val userName: String,
    val startTime: String,
    val endTime: String,
    @IntRange(from = 0, to = 3) val status: Int,
    val timeLength: String
) : Comparable<TrainingRecord> {

    companion object {

        const val TABLE_NAME = "training_record"

        /**
         * 记录状态：未完成。
         */
        const val STATUS_UNFINISHED = 0

        /**
         * 记录状态：已完成但是无效。
         */
        const val STATUS_NOT_VALID = 1

        /**
         * 记录状态：已完成，有效但是还没有被记录。
         */
        const val STATUS_NOT_RECORDED = 2

        /**
         * 记录状态：已经被记录。
         */
        const val STATUS_RECORDED = 3
    }

    override fun compareTo(other: TrainingRecord) = this.id - other.id
}