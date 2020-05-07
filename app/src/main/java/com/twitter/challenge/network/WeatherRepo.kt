package com.twitter.challenge.network

import com.twitter.challenge.model.WeatherApi
import com.twitter.challenge.model.WeatherResponse

class WeatherRepo(private val api: WeatherApi) : BaseRepo() {

    suspend fun getCurrentWeather(): WeatherResponse? {
        return safeApiCall(
                call = { api.getCurrentWeatherAsync().await() },
                errorMessage = "Error occurred while retrieving current weather"
        )
    }

    suspend fun getFutureWeather(day: Int): WeatherResponse? {
        return safeApiCall(
                call = { api.getFutureWeatherAsync(day.toString()).await() },
                errorMessage = "Error occurred while retrieving data future weather"
        )
    }
}