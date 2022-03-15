package com.fly.sample_showgithubuser.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiManager {

    private const val CONNECT_TIME_OUT = 30L
    private const val READ_TIME_OUT = 60L
    private const val WRITE_TIME_OUT = 60L
    private const val baseUrl = "https://api.github.com";

    val apiService: ApiService by lazy {
        getRetrofit().create(ApiService::class.java)
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
            .build()
    }
}