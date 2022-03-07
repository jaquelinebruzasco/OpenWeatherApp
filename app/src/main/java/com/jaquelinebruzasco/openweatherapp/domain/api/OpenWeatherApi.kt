package com.jaquelinebruzasco.openweatherapp.domain.api

import com.jaquelinebruzasco.openweatherapp.domain.model.ApiConstants
import com.jaquelinebruzasco.openweatherapp.domain.model.ForecastModel
import com.jaquelinebruzasco.openweatherapp.domain.model.LocationModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("/geo/1.0/direct")
    suspend fun getLocation(
        @Query("q")
        locationName: String,
        @Query("appid")
        apiKey: String = ApiConstants.API_KEY
    ): Response<LocationModel>

    @GET("/data/2.5/weather")
    suspend fun getWeather(
        @Query("lat")
        latitude: Double,
        @Query("lon")
        longitude: Double,
        @Query("appid")
        apiKey: String = ApiConstants.API_KEY,
        @Query("units")
        unit: String = ApiConstants.UNIT
    ): Response<ForecastModel>
}