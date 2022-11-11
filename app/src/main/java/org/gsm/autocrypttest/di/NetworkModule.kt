package org.gsm.autocrypttest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.gsm.autocrypttest.BuildConfig
import org.gsm.autocrypttest.data.network.CenterApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule{

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            //서버로부터의 응답까지의 시간이 읽기 시간 초과보다 크면 요청 실패로 판단한다.
            .readTimeout(10, TimeUnit.SECONDS)
            //서버로 요청을 시작한 후 15초가 지날 때 까지 데이터가 안오면 에러로 판단한다.
            .connectTimeout(10, TimeUnit.SECONDS)
            // 얼마나 빨리 서버로 데이터를 받을 수 있는지 판단한다.
            .writeTimeout(15, TimeUnit.SECONDS)
            .// 이 클라이언트를 통해 오고 가는 네트워크 요청/응답을 로그로 표시하도록 합니다.
            addInterceptor(provideHttpLoggingInterceptor())
            .build()
    }


    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Singleton
    @Provides
    fun provideCenters(retrofit: Retrofit) : CenterApi = retrofit.create(CenterApi::class.java)






}