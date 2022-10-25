package com.example.moeen.ui.home

import com.example.moeen.network.api.ApiServices
import com.example.moeen.utils.resultWrapper.performSafeApiCall
import javax.inject.Inject

class HomeRepository @Inject constructor(private val api : ApiServices){

    suspend fun getHome() = performSafeApiCall { api.getHome() }
    suspend fun getPosts() = performSafeApiCall { api.getPosts() }
    suspend fun getProfile() = performSafeApiCall { api.getProfile() }
}