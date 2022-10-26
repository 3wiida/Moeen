package com.example.moeen.ui.home.transportServices.ambulance.locationSelection

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moeen.network.model.carsTypesResponse.CarsTypesResponse
import com.example.moeen.utils.resultWrapper.ApiResult
import com.example.moeen.utils.resultWrapper.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSelectionViewModel @Inject constructor(private val repo: LocationSelectionRepository) :
    ViewModel() {
    private var _dateMutableLiveData = MutableLiveData<String>()
    var date: LiveData<String> = _dateMutableLiveData

    private var _timeMutableLiveDate = MutableLiveData<String>()
    var time: LiveData<String> = _timeMutableLiveDate

    private var _carTypes: MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Empty)
    var carTypes: StateFlow<ApiResult> = _carTypes

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
                is ResultWrapper.Success -> {
                    val carSpinnerList = arrayListOf<String>()
                    for (car in response.results.data) {
                        carSpinnerList.add(car.name)
                    }
                    _carTypes.value = ApiResult.Success(data = response.results)
                }
            }
        }

    }
}