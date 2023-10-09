package com.example.weatherappassignmentpc.Models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappassignmentpc.Api.WeatherApiService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherViewModel : ViewModel() {
    private val weatherApiService: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.weatherbit.io/v2.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    private val apiKey = "0e7f1aa7483547bbb6b3094d44d4ae7d"

    private val _currentWeather = MutableLiveData<WeatherData>()
    val currentWeather: LiveData<WeatherData> = _currentWeather

    fun fetchCurrentWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val response = weatherApiService.getCurrentWeather(latitude, longitude, apiKey)
                if (response.isSuccessful) {
                    val weatherData = response.body()?.data?.firstOrNull()
                    if(weatherData != null){
                        _currentWeather.value = weatherData

                    }
                    else{
                        // Handle API error
                    }

                } else {
                    // Handle API error
                }
            } catch (e: Exception) {
                // Handle network or other errors
            }
        }
    }

    fun fetchCurrentWeatherByCity(city: String) {
        viewModelScope.launch {
            try {
                val response = weatherApiService.getCurrentWeatherByCity(city, apiKey)
                if (response.isSuccessful) {
                    val weatherData = response.body()?.data?.firstOrNull()
                    if(weatherData != null){
                        _currentWeather.value = weatherData
                    }
                    else{
                        // Handle API error
                    }

                } else {
                    // Handle API error
                }
            } catch (e: Exception) {
                // Handle network or other errors
            }
        }
    }
}
