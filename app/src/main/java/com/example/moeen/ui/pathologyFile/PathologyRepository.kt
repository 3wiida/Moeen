package com.example.moeen.ui.pathologyFile

import com.example.moeen.network.api.ApiServices
import com.example.moeen.ui.pathologyFile.pojo.NewProfileModel
import com.example.moeen.utils.resultWrapper.performSafeApiCall
import javax.inject.Inject

class PathologyRepository @Inject constructor(private val apiServices: ApiServices) {
    suspend fun getProfile() = performSafeApiCall { apiServices.getProfile() }
    suspend fun getChronicDiseases() = performSafeApiCall { apiServices.getChronicDiseases() }
    suspend fun updateProfile(newProfile: NewProfileModel) = performSafeApiCall {
        apiServices.updateProfile(
            newProfile.region_id,
            newProfile.blood_type,
            newProfile.name,
            newProfile.gender,
            newProfile.photo,
            newProfile.national_id,
            newProfile.nationality,
            newProfile.weight,
            newProfile.length,
            newProfile.insurance_number,
            newProfile.notes
        )
    }
}