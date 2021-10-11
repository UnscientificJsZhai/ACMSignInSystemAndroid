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
abstract class UserDao {

    /**
     * 获取指定的用户的用户信息。
     *
     * @param sessionHash 要获取的用户信息的Session的HashCode。
     * @return 用户信息的本地备份。
     */
    @Query("SELECT * FROM ${CurrentUser.TABLE_NAME} WHERE sessionHash = :sessionHash")
    abstract fun getCurrentUser(sessionHash: Int): LiveData<CurrentUser>

    /**
     * 更新指定用户信息。
     *
     * @param user 用户信息。
     */
    @Update
    abstract fun updateCurrentUser(user: CurrentUser)

    /**
     * 将用户信息存储到数据库中，作为缓存。
     *
     * @param user 要添加的用户信息。
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addCurrentUser(user: CurrentUser)

    /**
     * 删除指定的用户信息，释放空间。
     *
     * @param user 要删除的用户信息。
     */
    @Delete
    abstract fun deleteCurrentUser(user: CurrentUser)

    /**
     * 获取所有已经缓存到本地的用户信息，用于删除。
     *
     * @return 所有用户信息。
     */
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

    /**
     * 获取缓存的所有训练记录。
     *
     * @return 训练记录，按ID降序排列。
     */
    @Query("SELECT * FROM ${TrainingRecord.TABLE_NAME} ORDER BY id DESC")
    abstract fun getTrainingRecords(): LiveData<List<TrainingRecord>>

    /**
     * 获取目前已经缓存的训练记录中，ID最大的数值。
     *
     * @return ID的数字。
     */
    @Query("SELECT max(id) FROM ${TrainingRecord.TABLE_NAME}")
    abstract fun getBiggestRecordId(): Int

    /**
     * 查询没有保存的训练记录，用于向服务器再次请求更新内容。
     *
     * @return 所有状态为未完成和未记录的训练记录。
     */
    @Query("SELECT * FROM ${TrainingRecord.TABLE_NAME} WHERE status=0 or status=2")
    abstract fun getUnrecordedRecords(): List<TrainingRecord>

    /**
     * 添加一个记录到数据库。
     *
     * @param record 要添加的训练记录。
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addRecord(record: TrainingRecord)

    /**
     * 更新一个记录。
     *
     * @param record 要更新的记录。
     */
    @Update
    abstract fun updateRecord(record: TrainingRecord)
}