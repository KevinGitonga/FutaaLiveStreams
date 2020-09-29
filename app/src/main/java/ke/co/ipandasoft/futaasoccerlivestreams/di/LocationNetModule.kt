/*
 * *
 *  * Created by Kevin Gitonga on 5/5/20 1:15 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 5/5/20 12:34 PM
 *
 */

package ke.co.ipandasoft.futaasoccerlivestreams.di

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.orhanobut.hawk.Hawk
import ke.co.ipandasoft.futaasoccerlivestreams.BuildConfig
import ke.co.ipandasoft.futaasoccerlivestreams.data.LocationApiService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object LocationNetModule {

    //service instance
    val service: LocationApiService by lazy (LazyThreadSafetyMode.SYNCHRONIZED){
        val okHttpClient= provideOkHttpClient()
        provideLocationRetrofit(okHttpClient,BuildConfig.LOCATION_INFO_BASE_URL).create(LocationApiService::class.java)
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY }

        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logger)
            .build()
    }

    private fun provideLocationRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .build()
    }

}

