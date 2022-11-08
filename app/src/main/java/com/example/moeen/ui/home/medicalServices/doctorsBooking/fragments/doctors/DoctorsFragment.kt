package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments.doctors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
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

    private lateinit var binding: FragmentDoctorsBinding
    private lateinit var specializationsAdapter: DoctorsSpecializationsAdapter
    private val doctorsAdapter = DoctorsAdapter()
    private lateinit var citiesSpinnerAdapter: CitiesSpinnerAdapter
    private val regionsSpinnerAdapter = RegionsSpinnerAdapter()
    private val viewModel: DoctorsBookingViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doctors, container, false)

        binding.include.tvFragmentTitle.text = "الأطباء"
        binding.include.ivBackArrow.setOnClickListener {
            view?.findNavController()?.navigateUp()
        }

        specializationsAdapter = DoctorsSpecializationsAdapter(requireContext())
        viewModel.getSpecializations()
        viewModel.getCities()
        viewModel.getDoctors()


        // TODO: LifecycleScope is executed everytime the fragment is loaded, check the logs
        /** Get All Specialities */
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.specializationState.collect { state ->
                when (state) {
                    ApiResult.Empty -> {}
                    ApiResult.Loading -> withContext(Dispatchers.Main) {
                        loadingDialog().show()
                    }
                    is ApiResult.Failure -> withContext(Dispatchers.Main) {
                        loadingDialog().cancel()
                        showToast(requireContext(), R.string.unknownError.toString())
                    }
                    is ApiResult.Success<*> -> withContext(Dispatchers.Main) {
                        loadingDialog().cancel()

                        // TODO: Solve the first item problem
                        val result = (state.data as SpecializationsResponse).data
                        if (result.isNotEmpty()) {
                            if (specializationsAdapter.currentList.isEmpty()) {
                                if(result.first().id != -1) {
                                    result.add(0, SpecializationsResponse.Data(-1, "الكل", "", true))
                                    specializationsAdapter.submitList(result)
                                    binding.rvDoctorsSpecialization.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                                    binding.rvDoctorsSpecialization.adapter = specializationsAdapter
                                }
                            }
                        }
                    }
                }
            }
        }


        /** Get All Doctors */
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.doctorsState.collect { state ->
                when (state) {
                    ApiResult.Empty -> {}
                    ApiResult.Loading -> withContext(Dispatchers.Main) {
                        loadingDialog().show()
                    }
                    is ApiResult.Failure -> withContext(Dispatchers.Main) {
                        loadingDialog().cancel()
                        showToast(requireContext(), R.string.unknownError.toString())
                    }
                    is ApiResult.Success<*> -> withContext(Dispatchers.Main) {
                        loadingDialog().cancel()
                        val result = (state.data as DoctorsResponse).data

                        doctorsAdapter.doctorsList = result
                        binding.rvDoctors.adapter = doctorsAdapter
                        binding.rvDoctors.layoutManager = LinearLayoutManager(requireContext())
                    }
                }
            }
        }


        /** Get All Cities */
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.citiesState.collect { state ->
                when (state) {
                    ApiResult.Empty -> {}
                    ApiResult.Loading -> withContext(Dispatchers.Main) {
                        loadingDialog().show()
                    }
                    is ApiResult.Failure -> withContext(Dispatchers.Main) {
                        loadingDialog().cancel()
                        showToast(requireContext(), R.string.unknownError.toString())
                    }
                    is ApiResult.Success<*> -> withContext(Dispatchers.Main) {
                        val result = (state.data as CitiesResponse).data
                        result.add(0, CitiesResponse.Data(-1, "المدينة"))

                        citiesSpinnerAdapter = CitiesSpinnerAdapter(result)
                        binding.spCity.adapter = citiesSpinnerAdapter
                    }
                }
            }
        }


        /** Get All Regions */
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.regionsState.collect { state ->
                when (state) {
                    ApiResult.Empty -> {}
                    ApiResult.Loading -> withContext(Dispatchers.Main) {
                        loadingDialog().show()
                    }
                    is ApiResult.Failure -> withContext(Dispatchers.Main) {
                        loadingDialog().cancel()
                        showToast(requireContext(), R.string.unknownError.toString())
                    }
                    is ApiResult.Success<*> -> withContext(Dispatchers.Main) {
                        val result = (state.data as RegionsResponse).data!!
                        result.add(0, RegionsResponse.Data(null, -1, "الحى"))

                        regionsSpinnerAdapter.regionsList = result
                        binding.spNeighborhood.adapter = regionsSpinnerAdapter
                    }
                }
            }
        }


        /** Handle Speciality Click */
        specializationsAdapter.onItemClicked = { clicked ->
            if (clicked.name == "الكل") {
                viewModel.getDoctors()
                binding.tvDoctorsSpecializationTitle.text = "كل التخصصات"
            } else {
                viewModel.getDoctors(clicked.id)
                binding.tvDoctorsSpecializationTitle.text = clicked.name
            }

            val newList = specializationsAdapter.currentList.map { it.copy() }
            newList.forEach {
                it.checked = it.name == clicked.name
            }
            specializationsAdapter.submitList(newList)
        }


        /** Handle Cities Spinner Item Click */
        binding.spCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val city = citiesSpinnerAdapter.getCityId(position)
                viewModel.getRegions(city)
            }
        }


        /** Search For Doctors Button */
        binding.btnDoctorsSearch.setOnClickListener {
            val cityId = (binding.spCity.selectedItem as CitiesResponse.Data).id
            val regionId = (binding.spNeighborhood.selectedItem as RegionsResponse.Data).id

            if (cityId != -1 && regionId == -1) {
                viewModel.getDoctors(cityId = cityId)
            } else if (cityId == -1 && regionId != -1) {
                viewModel.getDoctors(regionId = regionId)
            } else if (cityId != -1 && regionId != -1) {
                viewModel.getDoctors(cityId = cityId, regionId = regionId)
            }

            binding.spCity.setSelection(0)
            binding.spNeighborhood.setSelection(0)
        }


        /** Handle Doctor Click */
        doctorsAdapter.onItemClicked = {
            view?.findNavController()
                ?.navigate(DoctorsFragmentDirections.actionDoctorsFragmentToDoctorProfileFragment(it))
        }


        return binding.root
    }
}