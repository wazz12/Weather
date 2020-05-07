package com.twitter.challenge.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.twitter.challenge.model.WeatherApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiFactory {

    private const val baseUrl = "https://twitter-code-challenge.s3.amazonaws.com/"

    //OkHttpClient for building http request url
    private val apiClient = OkHttpClient.Builder().build()

    private fun retrofit(): Retrofit = Retrofit.Builder()
            .client(apiClient)
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()


    val weatherApi: WeatherApi = retrofit().create(WeatherApi::class.java)
}