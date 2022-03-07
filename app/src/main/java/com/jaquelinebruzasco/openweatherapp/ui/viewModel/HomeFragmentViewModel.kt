package com.jaquelinebruzasco.openweatherapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaquelinebruzasco.openweatherapp.domain.api.OpenWeatherRepository
import com.jaquelinebruzasco.openweatherapp.domain.model.ForecastModel
import com.jaquelinebruzasco.openweatherapp.domain.model.LocationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
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
                    getForecast(latitude = it.latitude, longitude = it.longitude)
                } ?: kotlin.run { _weather.value = OpenWeatherState.Failure("") }
            } else {
                _weather.value = OpenWeatherState.Failure(response.message())
            }
        }
    }

    private suspend fun getForecast(latitude: Double, longitude: Double) {
        val response = repository.getWeather(latitude, longitude)
        if (response.isSuccessful) {
            response.body()?.let {
                OpenWeatherState.Success(it)
            } ?: kotlin.run { _weather.value = OpenWeatherState.Failure("") }
        } else {
            _weather.value = OpenWeatherState.Failure(response.message())
        }
    }
}

sealed class OpenWeatherState {
    object Idle: OpenWeatherState()
    object Loading : OpenWeatherState()
    class Success(val data: ForecastModel) : OpenWeatherState()
    class Failure(val message: String) : OpenWeatherState()
}