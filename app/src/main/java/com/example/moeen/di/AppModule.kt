package com.example.moeen.di

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.moeen.common.Constants.BASE_URL
import com.example.moeen.network.api.ApiServices
import com.example.moeen.utils.PrefUtils.PrefKeys.USER_TOKEN
import com.example.moeen.utils.PrefUtils.PrefUtils.Companion.getFromPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor {
            it.proceed(
                it.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + getFromPref(context, USER_TOKEN, ""))
                    .build()
            )
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): ApiServices {
        return retrofit.create(ApiServices::class.java)
    }

    @Provides
    @Singleton
    fun provideBundle():Bundle{
        return Bundle()
    }

}