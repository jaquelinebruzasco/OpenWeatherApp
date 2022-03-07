package com.jaquelinebruzasco.openweatherapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jaquelinebruzasco.openweatherapp.R
import com.jaquelinebruzasco.openweatherapp.databinding.FragmentHomeBinding
import com.jaquelinebruzasco.openweatherapp.domain.model.ForecastModel
import com.jaquelinebruzasco.openweatherapp.ui.viewModel.HomeFragmentViewModel
import com.jaquelinebruzasco.openweatherapp.ui.viewModel.OpenWeatherState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment: Fragment() {

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
                    is OpenWeatherState.Idle -> Unit
                    is OpenWeatherState.Loading -> {
                        // TODO: progress bar
                    }
                    is OpenWeatherState.Failure -> showFailureMessage(state.message)
                    is OpenWeatherState.Success -> loadForecastView(state.data)
                }

            }
        }

    }

    private fun loadForecastView(data: ForecastModel) {
        _binding.apply {
            tvForecastToday.text = resources.getString(R.string.fragment_home_forecast_today, etLocation.text.toString())
            tvForecastInfo.text = data.weather[0].forecast
            tvDescription.text = data.weather[0].description
        }
    }

    private fun showFailureMessage(message: String) {
        if (message.isEmpty()) {
            Snackbar.make(requireView(), resources.getString(R.string.location_error_message), Snackbar.LENGTH_LONG).show()
        } else {
            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
        }
    }


    private fun initView() {
        _binding.apply {
            btnLoadForecast.setOnClickListener { viewModel.loadLocation(etLocation.text.toString()) }
            btnDetails.setOnClickListener { findNavController().navigate(R.id.fragment_details) }
            btnDetails.visibility = View.GONE
            btnSunInfo.visibility = View.GONE
        }
    }
}