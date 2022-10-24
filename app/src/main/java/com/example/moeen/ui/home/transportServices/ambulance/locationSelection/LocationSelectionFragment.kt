package com.example.moeen.ui.home.transportServices.ambulance.locationSelection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.moeen.R
import com.example.moeen.base.BaseFragment
import com.example.moeen.databinding.FragmentLocationSelectionBinding
import com.example.moeen.utils.resultWrapper.ApiResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationSelectionFragment : BaseFragment() {
    lateinit var binding:FragmentLocationSelectionBinding
    private val viewModel:LocationSelectionViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_location_selection,container,false)
        binding.viewModel= viewModel
        binding.lifecycleOwner=viewLifecycleOwner
        viewModel.getCarTypes()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.backBtn.setOnClickListener{
            activity?.onBackPressed()
        }
    }

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
                        loadingDialog().cancel()
                    }
                }
            }
        }
    }
}