package com.twitter.challenge

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.twitter.challenge.utils.ProgressSpinnerDialog
import com.twitter.challenge.utils.isNetworkAvailable
import com.twitter.challenge.utils.showToast
import com.twitter.challenge.viewModel.WeatherViewModel
import kotlinx.android.synthetic.main.activity_main.*

class WeatherActivity : AppCompatActivity() {

    private lateinit var weatherViewModel: WeatherViewModel

    private var progressSpinnerDialog: ProgressSpinnerDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        progressSpinnerDialog = ProgressSpinnerDialog(this)
        next_days_weather.setOnClickListener { onNextClick() }
        setObservers()

        if (isNetworkAvailable(this)) {
            showProgressSpinnerDialog()
            weatherViewModel.getCurrentWeather()
        } else {
            showToast(this, R.string.no_network_error_message)
        }
    }

    override fun onStop() {
        super.onStop()

        weatherViewModel.cancelAllRequests()
        hideProgressSpinnerDialog()
    }

    private fun setObservers() {
        weatherViewModel.currentWeatherLiveData.observe(this, Observer {
            hideProgressSpinnerDialog()
            it?.let { weather ->
                Log.i("currentWeatherLiveData", weather.toString())
                setTemperature(weather.weather.temp)
                setWind(weather.wind.speed)
                setCloudImage(weather.clouds.cloudiness)
            } ?: showToast(this, R.string.error_message)
        })

        weatherViewModel.futureTemperatureLiveData.observe(this, Observer {
            hideProgressSpinnerDialog()
            it?.let { weatherList ->
                Log.i("futureTempraturLiveData", weatherList.toString())
                displayStdDev(weatherList)
            } ?: showToast(this, R.string.error_message)
        })
    }

    private fun onNextClick() {
        if (isNetworkAvailable(this)) {
            showProgressSpinnerDialog()
            weatherViewModel.getFutureWeather()
        } else {
            showToast(this, R.string.no_network_error_message)
        }
    }

    private fun setTemperature(temp: Double) {
        temperature_text_view.text = getString(R.string.temperature, temp, TemperatureConverter.celsiusToFahrenheit(temp.toFloat()))
    }

    private fun setWind(speed: Double) {
        wind_speed_text_view.text = speed.toString()
    }

    private fun setCloudImage(cloudiness: Double) {
        if (cloudiness > 50) {
            cloud_image_view.visibility = View.VISIBLE
        } else {
            cloud_image_view.visibility = View.GONE
        }
    }

    private fun displayStdDev(tempList: List<Double>) {
        val stdDev = weatherViewModel.calculateStdDev(tempList)
        next_days_weather.visibility = View.GONE
        std_dev_title_text_view.visibility = View.VISIBLE
        std_dev_temperature_text_view.visibility = View.VISIBLE
        std_dev_temperature_text_view.text = getString(R.string.temperature, stdDev, TemperatureConverter.celsiusToFahrenheit(stdDev.toFloat()))
    }

    private fun showProgressSpinnerDialog() {
        progressSpinnerDialog?.showDialog()
    }

    private fun hideProgressSpinnerDialog() {
        progressSpinnerDialog?.hideDialog()
    }
}