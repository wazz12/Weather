package com.twitter.challenge.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.twitter.challenge.model.WeatherResponse
import com.twitter.challenge.network.ApiFactory
import com.twitter.challenge.network.WeatherRepo
import kotlinx.coroutines.*
import java.text.DecimalFormat
import kotlin.coroutines.CoroutineContext
import kotlin.math.sqrt

class WeatherViewModel : ViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)
    private val weatherRepo: WeatherRepo = WeatherRepo(ApiFactory.weatherApi)

    val currentWeatherLiveData = MutableLiveData<WeatherResponse>()
    val futureTemperatureLiveData = MutableLiveData<List<Double>>()

    fun getCurrentWeather() {
        scope.launch {
            val currentWeather = weatherRepo.getCurrentWeather()
            currentWeatherLiveData.postValue(currentWeather)
        }
    }

    fun getFutureWeather() {
        scope.launch {
            val weatherList: MutableList<Double> = mutableListOf()
            for (i in 1 until 6) {
                val futureWeather = weatherRepo.getFutureWeather(i)
                futureWeather?.let { weather ->
                    weatherList.add(weather.weather.temp)
                }
            }
            futureTemperatureLiveData.postValue(weatherList)
        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()

    fun calculateStdDev(tempList: List<Double>): Double {
        val n = tempList.size
        val mean = tempList.sumByDouble { it } / n
        var totalSquareValue = 0.0
        for (temp in tempList) {
            val value = (temp - mean)
            val squareValue = value * value
            totalSquareValue += squareValue
        }
        val variance = totalSquareValue / (n - 1)
        val stdDev = sqrt(variance)
        val formatter = DecimalFormat("0.00")
        return formatter.format(stdDev).toDouble()
    }
}