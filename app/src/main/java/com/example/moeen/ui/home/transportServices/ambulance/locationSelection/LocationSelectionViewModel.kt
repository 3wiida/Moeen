package com.example.moeen.ui.home.transportServices.ambulance.locationSelection

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.*
import com.example.moeen.utils.FormErrors
import com.example.moeen.utils.resultWrapper.ApiResult
import com.example.moeen.utils.resultWrapper.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSelectionViewModel @Inject constructor(private val repo: LocationSelectionRepository) : ViewModel() {

    /** vars */
    private var _dateMutableLiveData = MutableLiveData<String>()
    var date: LiveData<String> = _dateMutableLiveData

    private var _timeMutableLiveDate = MutableLiveData<String>()
    var time: LiveData<String> = _timeMutableLiveDate

    private var _carTypes: MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Empty)
    var carTypes= _carTypes.asStateFlow()

    private var _calculatePriceResponse:MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Empty)
    var calculatePriceResponse=_calculatePriceResponse.asStateFlow()

    private var _calculateDistanceResponse:MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Empty)
    var calculateDistanceResponse = _calculateDistanceResponse.asStateFlow()

    var formErrors:ObservableArrayList<FormErrors> = ObservableArrayList()
    /** ----------------------------------------------------------------------------------------------*/



    /** show date picker dialog */
    fun showDatePickerDialog(context: Context) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, i, i2, i3 ->
                _dateMutableLiveData.postValue("$i-${if((i2 + 1) <10) "0${i2+1}" else i2+1}-${if (i3<10) "0$i3" else i3}")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }


    /** show time picker dialog */
    fun showTimePickerDialog(context: Context) {
        val time = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            context, { _, i, i2 ->
                _timeMutableLiveDate.postValue("$i2 : $i")
            }, time.get(Calendar.HOUR_OF_DAY),
            time.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }


    /** call to get car types*/
    fun getCarTypes() {
        _carTypes.value = ApiResult.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = repo.getCarTypes()) {
                is ResultWrapper.Failure -> _carTypes.value = ApiResult.Failure(message = response.message)
                is ResultWrapper.Success -> _carTypes.value = ApiResult.Success(data = response.results)
            }
        }
    }


    /** call to calculate trip price */
    fun calculatePrice(govId:Int,govArrivalId:Int,cityId:Int,cityArrivalId:Int,carTypeId:Int){
        _calculatePriceResponse.value=ApiResult.Loading
        viewModelScope.launch(Dispatchers.IO){
            when(val response=repo.calculatePrice(govId,govArrivalId, cityId, cityArrivalId, carTypeId)){
                is ResultWrapper.Failure -> _calculatePriceResponse.value=ApiResult.Failure(message = response.message)
                is ResultWrapper.Success -> _calculatePriceResponse.value=ApiResult.Success(data = response.results)
            }
        }
    }

    /** call to get Distance */
    fun calculateDistance(carTypeId: Int, startLat:String, startLong:String, endLat:String, endLong:String){
        _calculateDistanceResponse.value=ApiResult.Loading
        viewModelScope.launch(Dispatchers.IO){
            when(val response=repo.calculateDistance(carTypeId, startLat, startLong, endLat, endLong)){
                is ResultWrapper.Failure -> _calculateDistanceResponse.value=ApiResult.Failure(message = response.message)
                is ResultWrapper.Success -> _calculateDistanceResponse.value=ApiResult.Success(data= response.results)
            }
        }
    }


    /** check is all fields are correct and not empty */
    fun isFormValid(movingPlace:String,arrivalPlace:String,carType:String,date:String,time:String):Boolean {
        formErrors.clear()

        if(movingPlace.isEmpty()) formErrors.add(FormErrors.EMPTY_MOVING_PLACE)
        if(arrivalPlace.isEmpty()) formErrors.add(FormErrors.EMPTY_ARRIVAL_PLACE)
        if(carType.isEmpty()) formErrors.add(FormErrors.EMPTY_CAR_TYPE)
        if(date.isEmpty()) formErrors.add(FormErrors.INVALID_DATE)
        if(time.isEmpty()) formErrors.add(FormErrors.INVALID_TIME)

        return formErrors.isEmpty()
    }
}