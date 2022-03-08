package com.jaquelinebruzasco.openweatherapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jaquelinebruzasco.openweatherapp.R
import com.jaquelinebruzasco.openweatherapp.databinding.FragmentDetailsBinding
import com.jaquelinebruzasco.openweatherapp.domain.model.TemperatureInfoModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var _binding: FragmentDetailsBinding
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.apply {
            btnBack.setOnClickListener { findNavController().popBackStack() }
        }
        loadInfo(args.dataInfo)
    }

    private fun loadInfo(data: TemperatureInfoModel) {
        _binding.apply {
            tvTemperatureInfo.text = data.temperature.toString()
            tvFeelsLikeInfo.text = data.feelsLike.toString()
            tvHumidityInfo.text = data.humidity.toString()
        }
    }


}