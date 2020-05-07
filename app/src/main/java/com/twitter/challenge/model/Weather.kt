package com.twitter.challenge.model

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

data class WeatherResponse(
        var weather: Weather,
        var wind: Wind,
        var clouds: Clouds
)

data class Weather(
        var temp: Double
)

data class Wind(
        var speed: Double
)

data class Clouds(
        var cloudiness: Double
)

interface WeatherApi {

    @GET("current.json")
    fun getCurrentWeatherAsync(): Deferred<Response<WeatherResponse>>

    @GET("future_{day}.json")
    fun getFutureWeatherAsync(@Path("day") day: String): Deferred<Response<WeatherResponse>>
}