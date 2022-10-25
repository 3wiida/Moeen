package com.example.moeen.ui.pathologyFile.pojo

import retrofit2.http.Field

data class NewProfileModel(
    val region_id:String,
    val blood_type:String,
    val name:String,
    val gender:String,
    val photo: String?=null,
    val national_id: Int,
    val nationality: String,
    val weight: Int,
    val length: Int,
    val insurance_number: String
)