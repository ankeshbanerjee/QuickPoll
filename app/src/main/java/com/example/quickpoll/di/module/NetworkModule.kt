package com.example.quickpoll.di.module

import android.content.Context
import com.example.quickpoll.data.network.service.AuthService
import com.example.quickpoll.data.network.service.PollService
import com.example.quickpoll.data.network.service.UploadService
import com.example.quickpoll.data.network.service.UserService
import com.example.quickpoll.utils.Constants
import com.example.quickpoll.utils.PreferencesDataStoreHelper
import com.example.quickpoll.utils.PreferencesDataStoreKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun providesOkHttpClient(@ApplicationContext applicationContext: Context) =
        OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)
            .addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder()
                runBlocking {
                    val authToken = PreferencesDataStoreHelper.retrieveData(
                        applicationContext,
                        PreferencesDataStoreKey.AUTH_TOKEN
                    ).firstOrNull()
                    if (authToken != null) {
                        request.addHeader("Authorization", "Bearer $authToken")
                    }
                }
                return chain.proceed(request.build())
            }
        }).build()

    @Provides
    @Singleton
    fun providesRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .build()

    @Provides
    @Singleton
    fun providesAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun providesUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun providesPollService(retrofit: Retrofit): PollService =
        retrofit.create(PollService::class.java)

    @Provides
    @Singleton
    fun providesUploadService(retrofit: Retrofit): UploadService =
        retrofit.create(UploadService::class.java)
}