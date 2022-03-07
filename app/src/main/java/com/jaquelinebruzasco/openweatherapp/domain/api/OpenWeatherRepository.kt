package com.jaquelinebruzasco.openweatherapp.domain.api

import com.jaquelinebruzasco.openweatherapp.domain.model.ForecastModel
import com.jaquelinebruzasco.openweatherapp.domain.model.LocationModel
import retrofit2.Response
import javax.inject.Inject

class OpenWeatherRepository @Inject constructor(
    private val api: OpenWeatherApi
) {
    suspend fun getLocation(locationName: String): Response<LocationModel> {
        return api.getLocation(locationName)
    }
    suspend fun getWeather(latitude: Double, longitude: Double): Response<ForecastModel> {
        return api.getWeather(latitude, longitude)
    }
}