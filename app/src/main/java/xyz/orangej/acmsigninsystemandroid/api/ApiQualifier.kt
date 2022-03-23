package xyz.orangej.acmsigninsystemandroid.api

import javax.inject.Qualifier

/**
 * 使用常规OkHttp客户端的修饰器。
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NormalOkHttpClient

/**
 * 使用测试API专用的OkHttp客户端的修饰器。
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TestOkHttpClient