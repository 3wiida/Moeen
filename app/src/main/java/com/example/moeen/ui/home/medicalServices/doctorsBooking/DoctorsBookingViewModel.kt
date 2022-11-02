package com.example.moeen.ui.home.medicalServices.doctorsBooking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moeen.utils.resultWrapper.ApiResult
import com.example.moeen.utils.resultWrapper.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorsBookingViewModel @Inject constructor(private val repo: DoctorsBookingRepository) : ViewModel() {

    private var _specializationState: MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Loading)
    var specializationState: StateFlow<ApiResult> = _specializationState

    private var _doctorsState: MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Loading)
    var doctorsState: StateFlow<ApiResult> = _doctorsState

    private var _citiesState: MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Loading)
    var citiesState: StateFlow<ApiResult> = _citiesState

    private var _regionsState: MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Loading)
    var regionsState: StateFlow<ApiResult> = _regionsState

    fun getSpecializations(){
        viewModelScope.launch(Dispatchers.IO){
            when(val response = repo.getSpecializations()){
                is ResultWrapper.Failure -> {
                    _specializationState.value = ApiResult.Failure(response.code, response.message)
                }
                is ResultWrapper.Success-> {
                    _specializationState.value = ApiResult.Success(response.results)
                }
            }
        }
    }

    fun getDoctors(id : Int? = null, cityId : Int? = null, regionId : Int? = null){
        viewModelScope.launch(Dispatchers.IO){
            when(val response = repo.getDoctors(id, cityId, regionId)){
                is ResultWrapper.Failure -> {
                    _doctorsState.value = ApiResult.Failure(response.code, response.message)
                }
                is ResultWrapper.Success-> {
                    _doctorsState.value = ApiResult.Success(response.results)
                }
            }
        }
    }

    fun getCities(){
        viewModelScope.launch(Dispatchers.IO){
            when(val response = repo.getCities()){
                is ResultWrapper.Failure -> {
                    _citiesState.value = ApiResult.Failure(response.code, response.message)
                }
                is ResultWrapper.Success-> {
                    _citiesState.value = ApiResult.Success(response.results)
                }
            }
        }
    }

    fun getRegions(id : Int?){
        viewModelScope.launch(Dispatchers.IO){
            when(val response = repo.getRegions(id)){
                is ResultWrapper.Failure -> {
                    _regionsState.value = ApiResult.Failure(response.code, response.message)
                }
                is ResultWrapper.Success-> {
                    _regionsState.value = ApiResult.Success(response.results)
                }
            }
        }
    }


}