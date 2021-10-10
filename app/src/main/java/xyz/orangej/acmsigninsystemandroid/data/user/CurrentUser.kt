package xyz.orangej.acmsigninsystemandroid.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 已经登录的用户信息。在数据库中缓存的数据。
 *
 * @param sessionHash 用户登录用到的session的哈希校验。实际需要获取哈希时则从Application对象中获取。
 * @param userName 账号。
 * @param displayName 用户的显示名称。
 * @param email 邮箱。
 * @param department 所属小组。
 * @param major 方向。
 * @param joinTime 加入时间。
 * @param allTrainingTime 训练时间总和。
 */
@Entity(tableName = CurrentUser.TABLE_NAME)
data class CurrentUser(
    @PrimaryKey val sessionHash: Int,
    val userName: String,
    val displayName: String,
    val email: String,
    val department: String,
    val major: String,
    val joinTime: String,
    val allTrainingTime: String
) {

    companion object {

        const val TABLE_NAME = "current_user"
    }
}