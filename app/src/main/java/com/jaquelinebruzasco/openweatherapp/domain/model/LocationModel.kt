package com.jaquelinebruzasco.openweatherapp.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LocationModel(
    @SerializedName("name")
    val locationName: String,
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lon")
    val longitude: Double
): Serializable