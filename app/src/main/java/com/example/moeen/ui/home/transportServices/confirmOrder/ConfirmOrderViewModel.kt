package com.example.moeen.ui.home.transportServices.confirmOrder

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moeen.common.Constants.TAG
import com.example.moeen.ui.home.transportServices.confirmOrder.pojo.OrderBody
import com.example.moeen.utils.FormErrors
import com.example.moeen.utils.resultWrapper.ApiResult
import com.example.moeen.utils.resultWrapper.ResultWrapper
import com.google.android.gms.common.api.Api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmOrderViewModel @Inject constructor(private val repo:ConfirmOrderRepository):ViewModel(){
    /** vars */
    var formErrors = ObservableArrayList<FormErrors>()
    private var _makeOrderResponse:MutableStateFlow<ApiResult> =MutableStateFlow(ApiResult.Empty)
    var makeOrderResponse=_makeOrderResponse.asStateFlow()
    /**-------------------------------------------------------------------------------*/

    /** validate form */
    fun validateForm(name:String,phoneNumber: String):Boolean{
        formErrors.clear()
        if(name.isEmpty()){
            formErrors.add(FormErrors.INVALID_NAME)
            Log.d(TAG, "validateForm: addedInvalidName")
        }

        if(phoneNumber.isEmpty() || phoneNumber.length !=11){
            formErrors.add(FormErrors.INVALID_PHONE)
            Log.d(TAG, "validateForm: addedInvalidPhone")
        }

        return formErrors.isEmpty()
    }

    /** call for make order */
    fun makeOrder(orderBody: OrderBody){
        _makeOrderResponse.value=ApiResult.Loading
        viewModelScope.launch(Dispatchers.IO){
            when(val response=repo.makeTransportOrder(orderBody)){
                is ResultWrapper.Failure -> _makeOrderResponse.value=ApiResult.Failure(message = response.message)
                is ResultWrapper.Success -> _makeOrderResponse.value=ApiResult.Success(data = response.results)
            }
        }
    }
}