package com.jaquelinebruzasco.openweatherapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaquelinebruzasco.openweatherapp.domain.api.OpenWeatherRepository
import com.jaquelinebruzasco.openweatherapp.domain.model.ForecastModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val repository: OpenWeatherRepository
): ViewModel() {

    private val _weather = MutableStateFlow<OpenWeatherState>(OpenWeatherState.Idle)
    val weather: StateFlow<OpenWeatherState> = _weather


    fun loadLocation(location: String) {
        viewModelScope.launch {
            _weather.value = OpenWeatherState.Loading
            val response = repository.getLocation(location)
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.isNullOrEmpty()) {
                       checkError(response.errorBody())
                    } else {
                        getForecast(latitude = it[0].latitude, longitude = it[0].longitude)
                    }
                } ?: kotlin.run { _weather.value = OpenWeatherState.Failure("") }
            } else {
                checkError(response.errorBody())
            }
        }
    }

    fun getForecast(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val response = repository.getWeather(latitude, longitude)
            if (response.isSuccessful) {
                response.body()?.let {
                    _weather.value = OpenWeatherState.Success(it)
                } ?: kotlin.run { _weather.value = OpenWeatherState.Failure("") }
            } else {
                checkError(response.errorBody())
            }
        }
    }

    private fun checkError(errorResponse: ResponseBody?) {
        if (errorResponse == null) {
            _weather.value = OpenWeatherState.Failure("")
        } else {
            _weather.value = OpenWeatherState.Failure(errorResponse.string())
        }
    }
}

sealed class OpenWeatherState {
    object Idle: OpenWeatherState()
    object Loading : OpenWeatherState()
    class Success(val data: ForecastModel) : OpenWeatherState()
    class Failure(val message: String) : OpenWeatherState()
}