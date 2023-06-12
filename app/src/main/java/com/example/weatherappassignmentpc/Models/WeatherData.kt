package com.example.weatherappassignmentpc.Models

data class WeatherData(
    val city_name: String,
    val temp: Double,
    val rh: Int,
    val wind_spd: Double,
    val app_temp: Double,
    val precip: Int,
    val datetime: String,
    val weather: WeatherDetails
)

data class WeatherDetails(
    val description: String,
    val code: Int,
    val icon: String
)
