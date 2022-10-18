package com.example.moeen.ui.Login

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moeen.common.Constants.TAG
import com.example.moeen.common.Constants.auth
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val repo: LoginRepository,bundle: Bundle) : ViewModel() {

    //Mutable State Flow Section
    private var _otpTimer: MutableStateFlow<Int> = MutableStateFlow(59)
    var otpTimer: StateFlow<Int> = _otpTimer

    private var _authValidation: MutableLiveData<String> = MutableLiveData()
    var authValidation: LiveData<String> = _authValidation

    //-----------------------------------------------------------------------------------


    //Logic Section

    fun checkPhoneValidation(phone: EditText): Boolean {
        return !(phone.text.toString()[0] != '0' || phone.text.toString().length != 11 || phone.text.toString().isEmpty())
    }

    fun startResendOtpTimer() {
        viewModelScope.launch(Dispatchers.Default) {
            var i = 60
            while (i > 0) {
                _otpTimer.value = i - 1
                i -= 1
                delay(1000L)
            }
        }
    }

    fun sendOtpCode(context: Context, phone: String) {
        var options = PhoneAuthOptions.newBuilder(auth).apply {
            setPhoneNumber(phone)
            setActivity(context as Activity)
            setTimeout(60L, TimeUnit.SECONDS)
            setCallbacks(callbacks)
        }.build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            signInWithCredential(p0)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            _authValidation.postValue(p0.message!!)
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            bundle.putString("OTPCode",p0)
            bundle.putParcelable("resendToken",p1)
        }
    }

    fun signInWithCredential(c: PhoneAuthCredential) {
        auth.signInWithCredential(c).addOnCompleteListener {
            if (it.isSuccessful) {
                _authValidation.postValue("Success")
            } else {
                if (it.exception is FirebaseAuthInvalidCredentialsException) {
                    _authValidation.postValue("Invalid")
                } else {
                    Log.d(TAG, "signInWithCredential: ${it.exception?.message}")
                }
            }
        }
    }


    fun login(phone:String,country:String,token:String){
        viewModelScope.launch(Dispatchers.IO){
            repo.api.loginUser(phone, country, token)
        }
    }
}