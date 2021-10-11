package xyz.orangej.acmsigninsystemandroid.data.user.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.*
import xyz.orangej.acmsigninsystemandroid.data.user.CurrentUser
import xyz.orangej.acmsigninsystemandroid.data.user.TrainingRecord

/**
 * 查询数据库中缓存数据的Dao接口。
 *
 * @see UserInformationDatabase
 */
@Dao
abstract class UserDao(database: RoomDatabase) {

    @Query("SELECT * FROM ${CurrentUser.TABLE_NAME} WHERE sessionHash = :sessionHash")
    abstract fun getCurrentUser(sessionHash: Int): LiveData<CurrentUser>

    @Update
    abstract fun updateCurrentUser(user: CurrentUser)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addCurrentUser(user: CurrentUser)

    @Delete
    abstract fun deleteCurrentUser(user: CurrentUser)

    @Query("SELECT * FROM ${CurrentUser.TABLE_NAME}")
    protected abstract fun getAllUser(): List<CurrentUser>

    /**
     * 删除数据库中的用户的缓存。
     */
    @WorkerThread
    fun deleteUserCache() {
        val users = getAllUser()
        for (user in users) {
            deleteCurrentUser(user)
        }
    }

    @Query("SELECT * FROM ${TrainingRecord.TABLE_NAME} ORDER BY id DESC")
    abstract fun getTrainingRecords(): LiveData<List<TrainingRecord>>

    @Query("SELECT max(id) FROM ${TrainingRecord.TABLE_NAME}")
    abstract fun getBiggestRecordId(): Int

    @Query("SELECT * FROM ${TrainingRecord.TABLE_NAME} WHERE status=0 or status=2")
    abstract fun getUnrecordedRecords(): List<TrainingRecord>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addRecord(record: TrainingRecord)

    @Update
    abstract fun updateRecord(record: TrainingRecord)

    @Delete
    abstract fun deleteRecord(record: TrainingRecord)
}