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

    /**
     * 提供常规OkHttpClient。这个Client会对每个请求添加ContentType和User-Agent头。
     *
     * @return 构造好的OkHttpClient。
     */
    @Provides
    @Singleton
    @NormalOkHttpClient
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .removeHeader("User-Agent")
                .addHeader("ContentType", "application/x-www-form-urlencoded")
                .addHeader("User-Agent", "android")
                .build()
            chain.proceed(request)
        }.build()

    /**
     * 提供仅供测试API的OkHttpClient。这个Client只能用来测试API，不应该用它来发送其它请求。
     */
    @Provides
    @Singleton
    @TestOkHttpClient
    fun provideTestClient() = OkHttpClient()

    /**
     * 提供Retrofit对象。
     *
     * @param okHttpClient 需要提供一个OkHttpClient。
     * @param context 应用程序级Context。
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        @NormalOkHttpClient okHttpClient: OkHttpClient,
        @ApplicationContext context: Context
    ): HttpApi = Retrofit.Builder()
        .baseUrl(getServerRoot(context))
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(HttpApi::class.java)

    /**
     * 从SharedPreference中读取服务器地址信息。
     *
     * @return 服务器地址。在存储之前就先确认过作为URL的合法性。
     */
    fun getServerRoot(context: Context): String {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
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