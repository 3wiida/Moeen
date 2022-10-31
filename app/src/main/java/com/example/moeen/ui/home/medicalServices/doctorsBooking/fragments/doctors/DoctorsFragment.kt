package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments.doctors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moeen.R
import com.example.moeen.base.BaseFragment
import com.example.moeen.databinding.FragmentDoctorsBinding
import com.example.moeen.network.model.citiesResponse.CitiesResponse
import com.example.moeen.network.model.doctorsResponse.DoctorsResponse
import com.example.moeen.network.model.regionsResponse.RegionsResponse
import com.example.moeen.network.model.specializationsResponse.SpecializationsResponse
import com.example.moeen.ui.home.medicalServices.doctorsBooking.DoctorsBookingViewModel
import com.example.moeen.utils.resultWrapper.ApiResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DoctorsFragment : BaseFragment() {

    private lateinit var binding : FragmentDoctorsBinding
    private lateinit var specializationsAdapter : DoctorsSpecializationsAdapter
    private val doctorsAdapter = DoctorsAdapter()
    private lateinit var citiesSpinnerAdapter: CitiesSpinnerAdapter
    private val regionsSpinnerAdapter = RegionsSpinnerAdapter()
    private val viewModel : DoctorsBookingViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doctors, container, false)

        specializationsAdapter = DoctorsSpecializationsAdapter(requireContext())
        viewModel.getCities()
        viewModel.getSpecializations()
        viewModel.getDoctors()

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.specializationState.collect { state ->
                when(state){
                    ApiResult.Empty -> {}
                    ApiResult.Loading -> withContext(Dispatchers.Main){
                        loadingDialog().show()
                    }
                    is ApiResult.Failure -> withContext(Dispatchers.Main){
                        loadingDialog().cancel()
                        showToast(requireContext(), R.string.unknownError.toString())
                    }
                    is ApiResult.Success<*> -> withContext(Dispatchers.Main){
                        loadingDialog().cancel()
                        val result = (state.data as SpecializationsResponse).data
                        result.add(0, SpecializationsResponse.Data(-1, "الكل", "", true))

                        specializationsAdapter.specializationsList = result
                        binding.rvDoctorsSpecialization.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        binding.rvDoctorsSpecialization.adapter = specializationsAdapter
                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.doctorsState.collect { state ->
                when(state){
                    ApiResult.Empty -> {}
                    ApiResult.Loading -> withContext(Dispatchers.Main){
                        loadingDialog().show()
                    }
                    is ApiResult.Failure -> withContext(Dispatchers.Main){
                        loadingDialog().cancel()
                        showToast(requireContext(), R.string.unknownError.toString())
                    }
                    is ApiResult.Success<*> -> withContext(Dispatchers.Main){
                        loadingDialog().cancel()
                        val result = (state.data as DoctorsResponse).data

                        doctorsAdapter.doctorsList = result
                        binding.rvDoctors.adapter = doctorsAdapter
                        binding.rvDoctors.layoutManager = LinearLayoutManager(requireContext())
                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO){
            viewModel.citiesState.collect{ state ->
                when(state){
                    ApiResult.Empty -> {}
                    ApiResult.Loading -> withContext(Dispatchers.Main){
                        loadingDialog().show()
                    }
                    is ApiResult.Failure -> withContext(Dispatchers.Main){
                        loadingDialog().cancel()
                        showToast(requireContext(), R.string.unknownError.toString())
                    }
                    is ApiResult.Success<*> -> withContext(Dispatchers.Main){
                        val result = (state.data as CitiesResponse).data
                        result.add(0, CitiesResponse.Data(-1, "المدينة"))

                        citiesSpinnerAdapter = CitiesSpinnerAdapter(result)
                        binding.spCity.adapter = citiesSpinnerAdapter
                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO){
            viewModel.regionsState.collect{ state ->
                when(state){
                    ApiResult.Empty -> {}
                    ApiResult.Loading -> withContext(Dispatchers.Main){
                        loadingDialog().show()
                    }
                    is ApiResult.Failure -> withContext(Dispatchers.Main){
                        loadingDialog().cancel()
                        showToast(requireContext(), R.string.unknownError.toString())
                    }
                    is ApiResult.Success<*> -> withContext(Dispatchers.Main){
                        val result = (state.data as RegionsResponse).data
                        result.add(0, RegionsResponse.Data(null , -1, "الحى"))

                        regionsSpinnerAdapter.regionsList = result
                        binding.spNeighborhood.adapter = regionsSpinnerAdapter
                    }
                }
            }
        }

        specializationsAdapter.onItemClicked = {
            if(it.name == "الكل"){
                viewModel.getDoctors()
            }else {
                viewModel.getDoctors(it.id)
            }
            it.checked = true
        }


        binding.spCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                if (position == 0) {
//                    binding.spCity.setSelection(0)
//                }else {
                    val city = citiesSpinnerAdapter.getCityId(position)
                    viewModel.getRegions(city)
//                }
            }

        }


        return binding.root
    }
}