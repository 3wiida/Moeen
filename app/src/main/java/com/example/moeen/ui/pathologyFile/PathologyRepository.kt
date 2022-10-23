package com.example.moeen.ui.pathologyFile

import com.example.moeen.network.api.ApiServices
import com.example.moeen.utils.resultWrapper.performSafeApiCall
import javax.inject.Inject

class PathologyRepository @Inject constructor(private val apiServices: ApiServices) {
    suspend fun getProfile()= performSafeApiCall { apiServices.getProfile() }
    suspend fun getChronicDiseases() = performSafeApiCall { apiServices.getChronicDiseases() }
}