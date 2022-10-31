package com.example.moeen.ui.home.medicalServices.doctorsBooking

import com.example.moeen.network.api.ApiServices
import com.example.moeen.utils.resultWrapper.performSafeApiCall
import javax.inject.Inject

class DoctorsBookingRepository @Inject constructor(val apiServices: ApiServices) {

    suspend fun getSpecializations() = performSafeApiCall { apiServices.getSpecializations() }
    suspend fun getDoctors(id : Int?) = performSafeApiCall { apiServices.getDoctors(id) }
    suspend fun getCities() = performSafeApiCall { apiServices.getCities() }
    suspend fun getRegions(id : Int?) = performSafeApiCall { apiServices.getRegions(id) }
}