package xyz.orangej.acmsigninsystemandroid

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import xyz.orangej.acmsigninsystemandroid.data.login.LoginDataSource
import xyz.orangej.acmsigninsystemandroid.data.login.LoginRepository
import kotlin.reflect.KProperty

/**
 * 全局Application类。
 */
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
     * 为登录服务提供属性委托。
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): LoginRepository =
        this.loginRepository
}