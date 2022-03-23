package xyz.orangej.acmsigninsystemandroid.data.user.database

import androidx.room.Database
import androidx.room.RoomDatabase
import xyz.orangej.acmsigninsystemandroid.data.user.CurrentUser
import xyz.orangej.acmsigninsystemandroid.data.user.TrainingRecord

/**
 * 数据库基类。
 */
@Database(
    entities = [CurrentUser::class, TrainingRecord::class],
    version = 1
)
abstract class UserInformationDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}