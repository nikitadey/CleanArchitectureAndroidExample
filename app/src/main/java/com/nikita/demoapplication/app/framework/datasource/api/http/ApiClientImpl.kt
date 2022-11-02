package com.nikita.demoapplication.app.framework.datasource.api.http

import com.google.gson.GsonBuilder
import com.nikita.demoapplication.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClientImpl {


    private val client by lazy {
        if (BuildConfig.DEBUG) {
            OkHttpClient.Builder().addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            ).connectTimeout(3000, TimeUnit.SECONDS)
                .build()
        } else {
            OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.SECONDS)
                .build()
        }
    }
    private val converter: GsonConverterFactory by lazy {
        GsonConverterFactory.create(
            GsonBuilder().setLenient().serializeNulls().create()
        )
    }
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://finnhub.io/api/v1/")
            .client(client)
            .addConverterFactory(converter)
            .build()
    }


    val api by lazy {
        retrofit.create(ApiClient::class.java)
    }


    private val retrofitWeb: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://finnhub.io/api/v1/")
            .client(client)
            .addConverterFactory(converter)
            .build()
    }


    val apiWeb by lazy {
        retrofitWeb.create(ApiClient::class.java)
    }
}