package xyz.orangej.acmsigninsystemandroid

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.HiltAndroidApp
import xyz.orangej.acmsigninsystemandroid.data.login.LoginDataSource
import xyz.orangej.acmsigninsystemandroid.data.login.LoginRepository
import xyz.orangej.acmsigninsystemandroid.data.user.database.UserInformationDatabase
import javax.inject.Inject
import kotlin.reflect.KProperty

/**
 * 全局Application类。
 */
@HiltAndroidApp
class SystemApplication : Application() {

    companion object {

        /**
         * 主要SP的名称。
         */
        const val SHARED_PREFERENCE_NAME = "main"
    }

    lateinit var loginRepository: LoginRepository
        private set
    private lateinit var mainStorage: SharedPreferences

    @Inject
    lateinit var database: UserInformationDatabase
        @JvmName("_getDatabase") get

    /**
     * 保存的session数据。
     */
    var session: String
        get() = this.mainStorage.getString("session", "") ?: ""
        set(value) {
            this.mainStorage.edit().putString("session", value).apply()
        }

    override fun onCreate() {
        super.onCreate()
        this.loginRepository = LoginRepository(LoginDataSource())
        getDatabase()

        val mainKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        this.mainStorage = EncryptedSharedPreferences.create(
            SHARED_PREFERENCE_NAME,
            mainKeyAlias,
            this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /**
     * 获取数据库对象。
     *
     * @return 数据库对象。
     */
    fun getDatabase() = this.database

    /**
     * 为登录服务提供属性委托。
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): LoginRepository =
        this.loginRepository
}