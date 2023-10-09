package com.example.weatherappassignmentpc.Activities


import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import com.example.weatherappassignmentpc.Models.WeatherData
import com.example.weatherappassignmentpc.Models.WeatherViewModel
import com.example.weatherappassignmentpc.R

class MainActivity : AppCompatActivity() {

    private lateinit var currentCityTextView: TextView
    private lateinit var currentTemperatureTextView: TextView
    private lateinit var currentHumidityTextView: TextView
    private lateinit var currentWindTextView: TextView
    private lateinit var currentWeatherDescriptionTextView: TextView
    private lateinit var searchView: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var precipitation:TextView
    private lateinit var feesLike:TextView
    private lateinit var dateTime:TextView
    private lateinit var weatherAnimation: ImageView

    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Initializing UI elements
        currentCityTextView = findViewById(R.id.currentCityTextView)
        currentTemperatureTextView = findViewById(R.id.currentTemperatureTextView)
        currentHumidityTextView = findViewById(R.id.currentHumidityTextView)
        currentWindTextView = findViewById(R.id.currentWindTextView)
        currentWeatherDescriptionTextView = findViewById(R.id.currentWeatherDescriptionTextView)
        precipitation=findViewById(R.id.precipitation)
        feesLike=findViewById(R.id.feelsLikeTemp)
        searchView = findViewById(R.id.searchView)
        progressBar=findViewById(R.id.progressBar)
        dateTime=findViewById(R.id.dateTime)
        weatherAnimation=findViewById(R.id.weatherView)

        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]


        // Set up weather data observer
        weatherViewModel.currentWeather.observe(this) { weatherData ->
            // Update UI with weather data
            updateWeatherUI(weatherData)
        }



        // Getting User's Location and Fetching Weather Data
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {

                // Fetch weather data using the location coordinates
                weatherViewModel.fetchCurrentWeather(location.latitude, location.longitude)

                // Stop listening for location updates
                locationManager.removeUpdates(this)
            }


            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        // Check location permission and request updates
        if (ContextCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestSingleUpdate(
                LocationManager.NETWORK_PROVIDER,
                locationListener,
                null
            )
        } else {
            // Request location permission from the user
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        // Set up search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Fetch weather data for the searched city
                fetchWeatherByCity(query)
                searchView.clearFocus()
                progressBar.visibility=View.VISIBLE
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Handle text change if needed
                return true
            }
        })
    }
    private fun fetchWeatherByCity(city: String) {
        weatherViewModel.fetchCurrentWeatherByCity(city)

    }

    private fun updateWeatherUI(weatherData: WeatherData?) {
        weatherData?.let {

            currentCityTextView.text = it.city_name
            dateTime.text="Today,${it.datetime}"
            currentTemperatureTextView.text = "${(it.temp.toInt())}°"
            currentHumidityTextView.text = "${it.rh}%"
            currentWindTextView.text = "${it.wind_spd} km/h"
            currentWeatherDescriptionTextView.text = it.weather.description
            precipitation.text="${it.precip}%"
            feesLike.text="${it.app_temp}°C"
            when(it.weather.code){
                in 200..232->weatherAnimation.setImageResource(R.drawable.thunderstorm)
                in 300..321->weatherAnimation.setImageResource(R.drawable.drizzle)
                in 500..531->weatherAnimation.setImageResource(R.drawable.rain)
                in 600..622->weatherAnimation.setImageResource(R.drawable.snow)
                in 701..781->weatherAnimation.setImageResource(R.drawable.mist)
                800->weatherAnimation.setImageResource(R.drawable.clearsky)
                in 801..804->weatherAnimation.setImageResource(R.drawable.clouds)
                else-> weatherAnimation.setImageResource(R.drawable.clearsky)
            }
            progressBar.visibility = View.GONE

        }
    }



    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}