package com.jaquelinebruzasco.openweatherapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jaquelinebruzasco.openweatherapp.R
import com.jaquelinebruzasco.openweatherapp.databinding.FragmentHomeBinding
import com.jaquelinebruzasco.openweatherapp.domain.model.ApiConstants
import com.jaquelinebruzasco.openweatherapp.domain.model.ForecastModel
import com.jaquelinebruzasco.openweatherapp.domain.model.SunTimeInfoModel
import com.jaquelinebruzasco.openweatherapp.ui.convertToDate
import com.jaquelinebruzasco.openweatherapp.ui.loadIcon
import com.jaquelinebruzasco.openweatherapp.ui.viewModel.HomeFragmentViewModel
import com.jaquelinebruzasco.openweatherapp.ui.viewModel.OpenWeatherState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeFragmentViewModel>()
    private lateinit var _binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservables()

    }

    private fun initObservables() {
        lifecycleScope.launchWhenCreated {
            viewModel.weather.collectLatest { state ->
                when (state) {
                    is OpenWeatherState.Idle -> hideProgressBar()
                    is OpenWeatherState.Loading -> showProgressBar()
                    is OpenWeatherState.Failure -> {
                        hideProgressBar()
                        showFailureMessage(state.message)
                    }
                    is OpenWeatherState.Success -> {
                        hideProgressBar()
                        loadForecastView(state.data)
                    }
                }

            }
        }

    }

    private fun loadForecastView(data: ForecastModel) {
        _binding.apply {
            tvForecastToday.text =
                resources.getString(R.string.fragment_home_forecast_today, data.name)
            tvForecastInfo.text = data.weather[0].forecast
            tvDescription.text = data.weather[0].description
            loadIcon(
                imageView = ivForecastIcon,
                code = data.weather[0].icon
            )
            btnDetails.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionFragmentHomeToFragmentDetails(data.temperature)
                findNavController().navigate(action)
            }
            btnSunInfo.setOnClickListener { showSunInformation(data.sunTime) }

            btnDetails.visibility = View.VISIBLE
            btnSunInfo.visibility = View.VISIBLE

        }
    }

    private fun showFailureMessage(message: String) {
        if (message.isEmpty()) {
            Snackbar.make(
                requireView(),
                resources.getString(R.string.location_error_message),
                Snackbar.LENGTH_LONG
            ).show()
        } else {
            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        _binding.apply {
            btnLoadForecast.setOnClickListener { viewModel.loadLocation(etLocation.text.toString()) }
            btnDetails.visibility = View.GONE
            btnSunInfo.visibility = View.GONE
        }
    }

    private fun showSunInformation(sunData: SunTimeInfoModel) {
        val alertDialog = this.context?.let { AlertDialog.Builder(it) }
        alertDialog?.apply {
            setTitle(R.string.fragment_home_sun_info)
            setMessage(
                resources.getString(
                    R.string.fragment_home_sunrise_sunset,
                    sunData.sunrise.convertToDate(),
                    sunData.sunset.convertToDate()
                )
            )
        }?.create()?.show()
    }

    private fun showProgressBar() {
        _binding.apply {
            progressCircular.visibility = View.VISIBLE
            layoutContent.visibility = View.GONE
        }
    }

    private fun hideProgressBar() {
        _binding.apply {
            progressCircular.visibility = View.GONE
            layoutContent.visibility = View.VISIBLE
        }
    }
}