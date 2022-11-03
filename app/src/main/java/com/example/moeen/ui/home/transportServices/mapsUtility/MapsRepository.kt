package com.example.moeen.ui.home.transportServices.mapsUtility

import com.example.moeen.network.api.ApiServices
import com.example.moeen.utils.resultWrapper.performSafeApiCall
import javax.inject.Inject

class MapsRepository @Inject constructor(private val apiServices: ApiServices) {
    suspend fun checkRegion(lat:String,lon:String) = performSafeApiCall { apiServices.checkOrderRegion(lat,lon) }
}