package com.example.moeen.ui.home.transportServices.ambulance.locationSelection

import com.example.moeen.network.api.ApiServices
import com.example.moeen.utils.resultWrapper.performSafeApiCall
import javax.inject.Inject

class LocationSelectionRepository @Inject constructor(private val apiServices: ApiServices) {
    suspend fun getCarTypes() = performSafeApiCall { apiServices.getCarTypes() }
}