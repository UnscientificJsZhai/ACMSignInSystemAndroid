package xyz.orangej.acmsigninsystemandroid

import android.app.Application
import xyz.orangej.acmsigninsystemandroid.data.login.LoginDataSource
import xyz.orangej.acmsigninsystemandroid.data.login.LoginRepository
import kotlin.reflect.KProperty

class SystemApplication : Application() {

    lateinit var loginRepository: LoginRepository
        private set

    override fun onCreate() {
        super.onCreate()
        this.loginRepository = LoginRepository(LoginDataSource())
    }

    /**
     * 为登录服务提供属性委托。
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): LoginRepository =
        this.loginRepository
}