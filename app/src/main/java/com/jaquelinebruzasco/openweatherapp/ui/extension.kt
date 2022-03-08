package com.jaquelinebruzasco.openweatherapp.ui

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jaquelinebruzasco.openweatherapp.domain.model.ApiConstants
import java.security.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun loadIcon(
    imageView: ImageView,
    code: String
) {
    Glide.with(imageView.context)
        .load("${ApiConstants.ICON_URL}$code${ApiConstants.ICON_EXTENSION_URL}")
        .into(imageView)
}

fun Int.convertToDate(): String = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.getDefault()).format(this * 1000L)