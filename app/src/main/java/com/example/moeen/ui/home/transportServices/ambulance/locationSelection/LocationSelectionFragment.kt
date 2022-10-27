package com.example.moeen.ui.home.transportServices.ambulance.locationSelection

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.moeen.R
import com.example.moeen.base.BaseFragment
import com.example.moeen.common.Constants.TAG
import com.example.moeen.databinding.FragmentLocationSelectionBinding
import com.example.moeen.network.model.carsTypesResponse.CarsTypesResponse
import com.example.moeen.ui.home.transportServices.ambulance.locationSelection.adapters.CarTypesSpinnerAdapter
import com.example.moeen.ui.home.transportServices.mapsUtility.MapsViewModel
import com.example.moeen.utils.resultWrapper.ApiResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationSelectionFragment : BaseFragment() {

    lateinit var binding:FragmentLocationSelectionBinding
    private val viewModel:LocationSelectionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCarTypes()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_location_selection,container,false)
        binding.viewModel= viewModel
        binding.lifecycleOwner=viewLifecycleOwner
        collectData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.movingPlaceEt.setOnClickListener {
            view.findNavController().navigate(R.id.action_locationSelectionFragment_to_mapsFragment)
        }

        binding.arrivalPlaceEt.setOnClickListener {
            view.findNavController().navigate(R.id.action_locationSelectionFragment_to_mapsFragment)
        }
    }


    /** collect flow data */
    private fun collectData(){
        lifecycleScope.launch {
            viewModel.carTypes.collect{ result->
                when(result){
                    ApiResult.Empty -> {}
                    is ApiResult.Failure -> {
                        loadingDialog().cancel()
                        showToast(requireContext(),result.message!!)
                    }
                    ApiResult.Loading -> {
                        loadingDialog().show()
                    }
                    is ApiResult.Success<*> -> {
                        val cars = result.data as CarsTypesResponse
                        binding.ambulanceCarTypeSpinner.adapter=CarTypesSpinnerAdapter(requireContext(),cars.data.toMutableList())
                        loadingDialog().cancel()
                    }
                }
            }
        }
    }
}