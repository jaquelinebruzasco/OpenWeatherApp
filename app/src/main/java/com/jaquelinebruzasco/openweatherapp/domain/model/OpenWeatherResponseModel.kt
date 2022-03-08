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
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String
): Serializable

data class TemperatureInfoModel(
    @SerializedName("temp")
    val temperature: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("humidity")
    val humidity: Double
): Serializable

data class SunTimeInfoModel(
    @SerializedName("sunrise")
    val sunrise: Int,
    @SerializedName("sunset")
    val sunset: Int
): Serializable