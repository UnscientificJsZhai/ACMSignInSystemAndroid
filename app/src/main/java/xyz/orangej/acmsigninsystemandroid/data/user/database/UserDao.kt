package xyz.orangej.acmsigninsystemandroid.data.user.database

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
interface UserDao {

    @Query("SELECT * FROM ${CurrentUser.TABLE_NAME} WHERE sessionHash = :sessionHash")
    fun getCurrentUser(sessionHash: Int): LiveData<CurrentUser>

    @Update
    fun updateCurrentUser(user: CurrentUser)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCurrentUser(user: CurrentUser)

    @Delete
    fun deleteCurrentUser(user: CurrentUser)

    @Query("SELECT * FROM ${TrainingRecord.TABLE_NAME}")
    fun getTrainingRecords(): LiveData<List<TrainingRecord>>
}