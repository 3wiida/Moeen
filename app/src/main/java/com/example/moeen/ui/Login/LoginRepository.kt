package com.example.moeen.ui.Login

import com.example.moeen.network.api.ApiServices
import com.example.moeen.utils.resultWrapper.performSafeApiCall
import javax.inject.Inject

class LoginRepository @Inject constructor(val api:ApiServices) {
    fun login(phone:String,country:String,token:String) = performSafeApiCall { api.loginUser(phone, country, token) }
}