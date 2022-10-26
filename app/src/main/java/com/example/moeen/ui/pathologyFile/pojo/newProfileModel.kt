package com.example.moeen.ui.pathologyFile.pojo

import retrofit2.http.Field

data class NewProfileModel(
    val region_id:Int=1,
    val blood_type:String="A+",
    val name:String="",
    val gender:String="male",
    val photo: String?=null,
    val national_id: String="",
    val nationality: String="",
    val weight: Int,
    val length: Int,
    val insurance_number: String="",
    val notes:String="",
    val d_o_b:String="1970-01-01"
)