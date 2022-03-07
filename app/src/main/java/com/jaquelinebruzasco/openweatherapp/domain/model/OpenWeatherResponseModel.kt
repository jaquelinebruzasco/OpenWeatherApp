package com.jaquelinebruzasco.openweatherapp.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ForecastModel(
    @SerializedName("weather")
    val weather: List<WeatherInfoModel>,
    @SerializedName("main")
    val temperature: TemperatureInfoModel,
    @SerializedName("sys")
    val sunTime: SunTimeInfoModel
): Serializable

data class WeatherInfoModel(
    @SerializedName("main")
    val forecast: String,
    @SerializedName("weather")
    val description: String,
    @SerializedName("weather")
    val icon: String
): Serializable

data class TemperatureInfoModel(
    @SerializedName("main")
    val temperature: Double,
    @SerializedName("main")
    val feelsLike: Double,
    @SerializedName("main")
    val humidity: Double
): Serializable

data class SunTimeInfoModel(
    @SerializedName("sys")
    val sunrise: Int,
    @SerializedName("sys")
    val sunset: Int
): Serializable