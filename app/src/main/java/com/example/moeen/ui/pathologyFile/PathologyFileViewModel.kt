package com.example.moeen.ui.pathologyFile

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moeen.network.model.profileResponse.ProfileResponse
import com.example.moeen.ui.pathologyFile.pojo.NewProfileModel
import com.example.moeen.utils.PrefUtils.PrefKeys.USER_TOKEN
import com.example.moeen.utils.PrefUtils.PrefUtils.Companion.getFromPref
import com.example.moeen.utils.resultWrapper.ApiResult
import com.example.moeen.utils.resultWrapper.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class PathologyFileViewModel @Inject constructor(val repo: PathologyRepository) : ViewModel() {

    //Variables Section
    private var _dateMutableLiveData=MutableLiveData<String>()
    var date:LiveData<String> =_dateMutableLiveData

    //get profile state flow
    private var _apiState :MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Empty)
    var apisate:StateFlow<ApiResult> = _apiState

    //chronic diseases state flow
    private var _chronicState:MutableStateFlow<ArrayList<String>> =MutableStateFlow(arrayListOf())
    var chronicState:StateFlow<ArrayList<String>> = _chronicState

    private var _updateProfile:MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Empty)
    var updateProfile:StateFlow<ApiResult> =_updateProfile
    /**------------------------------------------------------------------------------------------------------*/




    /** check is user logged in or not to determine which layout to show */
    fun isLoggedIn(context: Context): Boolean = getFromPref(context, USER_TOKEN, "") != ""




    /** show datePickerDialog when user click on editText to choose his birthday */
    fun showDataPickerDialog(context: Context){
        val calender = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, i, i2, i3 ->
                _dateMutableLiveData.postValue( "$i3/${i2 + 1}/$i")
            },
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }



    /** fun to make a call to get profile data from server */
    fun getProfile(){
        viewModelScope.launch(Dispatchers.IO){
            _apiState.value=ApiResult.Loading
            when(val response=repo.getProfile()){
                is ResultWrapper.Failure -> _apiState.value=ApiResult.Failure(message = response.message)
                is ResultWrapper.Success -> {
                    _apiState.value=ApiResult.Success(data = response.results)
                    _dateMutableLiveData.postValue(response.results.data.user.d_o_b)
                }
            }
        }
    }




    /** fun to get chronic diseases from the server */
    fun getChronicDiseases(){
        viewModelScope.launch(Dispatchers.IO){
            when(val response=repo.getChronicDiseases()){
                is ResultWrapper.Failure -> {}
                is ResultWrapper.Success -> {
                    var listOfDiseases= arrayListOf<String>()
                    for(i in response.results.data){
                        listOfDiseases.add(i.name)
                    }
                    _chronicState.value=listOfDiseases
                }
            }
        }
    }




    /** update profile function */
    fun updateProfile(model:NewProfileModel){
        _updateProfile.value=ApiResult.Loading
        viewModelScope.launch(Dispatchers.IO){
            when(val response=repo.updateProfile(model)){
                is ResultWrapper.Failure -> _updateProfile.value=ApiResult.Failure(message = response.message)
                is ResultWrapper.Success -> _updateProfile.value=ApiResult.Success(data = response.results)
            }
        }
    }
}