package xyz.orangej.acmsigninsystemandroid.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import xyz.orangej.acmsigninsystemandroid.data.user.database.UserInformationDatabase
import javax.inject.Singleton

/**
 * 为Room数据库提供Hilt依赖注入的Module。
 */
@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    /**
     * 提供数据库实例的方法。提供一个单例的数据库对象。
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UserInformationDatabase {
        return Room.databaseBuilder(
            context,
            UserInformationDatabase::class.java,
            "database.db"
        ).build()
    }
}