package com.example.moeen.ui.home.transportServices.ambulance.locationSelection

import com.example.moeen.network.api.ApiServices
import com.example.moeen.utils.resultWrapper.performSafeApiCall
import javax.inject.Inject

class LocationSelectionRepository @Inject constructor(private val apiServices: ApiServices) {

    suspend fun getCarTypes() = performSafeApiCall { apiServices.getCarTypes() }

    suspend fun calculatePrice(
        govId:Int,
        govArrivalId:Int,
        cityId:Int,
        cityArrivalId:Int,
        carTypeId:Int)
    = performSafeApiCall { apiServices.calculatePrice(govId,govArrivalId,cityId,cityArrivalId,carTypeId) }

    suspend fun calculateDistance(
        carTypeId: Int,
        startLat:String,
        startLong:String,
        endLat:String,
        endLong:String
    ) = performSafeApiCall { apiServices.calculateDistance(carTypeId,startLat,startLong,endLat,endLong) }
}