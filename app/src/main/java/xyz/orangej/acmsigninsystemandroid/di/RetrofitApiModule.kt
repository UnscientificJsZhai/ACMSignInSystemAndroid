package xyz.orangej.acmsigninsystemandroid.di

import android.content.Context
import android.util.Patterns
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import xyz.orangej.acmsigninsystemandroid.api.HttpApi
import xyz.orangej.acmsigninsystemandroid.api.NormalOkHttpClient
import xyz.orangej.acmsigninsystemandroid.api.TestOkHttpClient
import javax.inject.Singleton

/**
 * 为Retrofit提供Hilt依赖注入的Module。
 */
@InstallIn(SingletonComponent::class)
@Module
object RetrofitApiModule {

    @Provides
    @Singleton
    @NormalOkHttpClient
    fun provideOkHttpClient() = OkHttpClient()
        .newBuilder()
        .addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .removeHeader("User-Agent")
                .addHeader("ContentType", "application/x-www-form-urlencoded")
                .addHeader("User-Agent", "android")
                .build()
            chain.proceed(request)
        }.build()

    @Provides
    @Singleton
    @TestOkHttpClient
    fun provideTestClient() = OkHttpClient()

    @Provides
    @Singleton
    fun provideRetrofit(
        @NormalOkHttpClient okHttpClient: OkHttpClient,
        @ApplicationContext context: Context
    ): HttpApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(context.getServerRoot())
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()
        return retrofit.create(HttpApi::class.java)
    }

    /**
     * 从SharedPreference中读取服务器地址信息。
     *
     * @return 服务器地址。在存储之前就先确认过作为URL的合法性。
     */
    fun Context.getServerRoot(): String {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val serverAddress = sharedPreferences.getString("server", "https://www.orangej.xyz")!!
        return formatServerAddress(serverAddress)
    }

    /**
     * 格式化服务器地址。移除末尾的斜杠。
     *
     * @param original 输入服务器地址。
     * @return 格式化后的服务器地址。
     */
    fun formatServerAddress(original: String): String {
        return if (Patterns.WEB_URL.matcher(original).matches()) {
            var current = original
            while (current.endsWith("/")) {
                current = current.substring(0, current.lastIndex)
            }
            current
        } else {
            "https://www.orangej.xyz"
        }
    }
}