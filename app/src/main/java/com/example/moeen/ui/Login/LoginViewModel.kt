package com.example.moeen.ui.Login

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moeen.common.Constants.TAG
import com.example.moeen.common.Constants.auth
import com.example.moeen.utils.PrefUtils.PrefKeys.USER_TOKEN
import com.example.moeen.utils.PrefUtils.PrefUtils.Companion.saveInPref
import com.example.moeen.utils.resultWrapper.ApiResult
import com.example.moeen.utils.resultWrapper.ResultWrapper
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(@ApplicationContext val context: Context,val repo: LoginRepository, bundle: Bundle) : ViewModel() {

    //Mutable State Flow Section
    private var _apiState:MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Loading)
    var apiState:StateFlow<ApiResult> =_apiState

    private var otpStateFlow:MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Empty)
    fun getOtpStateFlow():StateFlow<ApiResult> = otpStateFlow

    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var mVerificationId=""
    lateinit var mResendToken: PhoneAuthProvider.ForceResendingToken

    //-----------------------------------------------------------------------------------


    //Logic Section

    fun checkPhoneValidation(phone: EditText): Boolean {
        return !(phone.text!!.isEmpty())
    }

    /*fun startResendOtpTimer() {
        viewModelScope.launch(Dispatchers.Default) {
            var i = 60
            while (i > 0) {
                _otpTimer.value = i - 1
                i -= 1
                delay(1000L)
            }
        }
    }*/

    fun startResendOtpTimer() = flow {
        var i=59
        while(i>=0){
            emit(i)
            i--
            delay(1000L)
        }
    }

    fun verifyCode(code:String,context:Activity){
        if(mVerificationId.isNotBlank()){
            otpStateFlow.value=ApiResult.Loading
            var credential=PhoneAuthProvider.getCredential(mVerificationId,code)
            signInWithPhoneAuthCredential(credential,context)
        }
    }

    fun initCallback(context: Activity){
        mCallbacks=object:PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(p0,context)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                otpStateFlow.value=ApiResult.Failure(null,p0.message.toString())
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                mVerificationId=p0
                mResendToken=p1
            }
        }
    }

    fun sendCode(phoneNumber:String,context: Activity){
        initCallback(context)
        var options=PhoneAuthOptions.newBuilder().apply {
            setPhoneNumber(phoneNumber)
            setTimeout(0L,TimeUnit.SECONDS)
            setActivity(context)
            setCallbacks(mCallbacks)
        }.build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun resendCode(phoneNumber:String,context: Activity){
        initCallback(context)
        var options=PhoneAuthOptions.newBuilder().apply {
            setPhoneNumber(phoneNumber)
            setTimeout(0L,TimeUnit.SECONDS)
            setActivity(context)
            setForceResendingToken(mResendToken)
            setCallbacks(mCallbacks)
        }.build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithPhoneAuthCredential(credential:PhoneAuthCredential,context: Activity){
        auth.signInWithCredential(credential).addOnCompleteListener{ task ->
            otpStateFlow.value=ApiResult.Empty
            if(task.isSuccessful){
                otpStateFlow.value=ApiResult.Success("success")
            }else{
                if(task.exception is FirebaseAuthInvalidCredentialsException){
                    otpStateFlow.value=ApiResult.Failure(message = "Invalid Code")
                    Log.d(TAG, "signInWithPhoneAuthCredential: ${task.exception?.message}")
                }else{
                    otpStateFlow.value=ApiResult.Failure(message = task.exception?.message.toString())
                }
            }
        }
    }


    //Api Calls ------------------------------------------------------------>>>>>>
    fun login(phone: String, country: String, token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when(val response = repo.login(phone, country, token)){
                is ResultWrapper.Failure -> _apiState.value=ApiResult.Failure(response.code,response.message)
                is ResultWrapper.Success -> {
                    saveInPref(context,USER_TOKEN,response.results.data.token)
                    _apiState.value=ApiResult.Success(response.results)
                }
            }
        }
    }

    fun getCountries() {
        viewModelScope.launch(Dispatchers.IO){
            when(val response =repo.getCountries()){
                is ResultWrapper.Failure -> _apiState.value=ApiResult.Failure(response.code,response.message)
                is ResultWrapper.Success -> _apiState.value=ApiResult.Success(response.results)
            }
        }
    }
}