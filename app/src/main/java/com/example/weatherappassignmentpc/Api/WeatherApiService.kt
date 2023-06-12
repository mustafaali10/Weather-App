package com.example.weatherappassignmentpc.Api

import com.example.weatherappassignmentpc.Models.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("current")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("key") apiKey: String
    ): Response<WeatherResponse>

    @GET("current")
    suspend fun getCurrentWeatherByCity(
        @Query("city") city: String,
        @Query("key") apiKey: String
    ): Response<WeatherResponse>
}

data class WeatherResponse(
    val data: List<WeatherData>
)
