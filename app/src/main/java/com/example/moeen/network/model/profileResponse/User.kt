package com.example.moeen.network.model.profileResponse

data class User(
    val accept: Int,
    val age: Int,
    val blood_type: Any,
    val chronic_diseases: List<Any>,
    val country_id: Int,
    val d_o_b: String,
    val gender: String,
    val gender_text: String,
    val hospital_client: Any,
    val id: Int,
    val insurance_number: Any,
    val length: Int,
    val name: String,
    val national_id: String,
    val nationality: String,
    val notes: Any,
    val phone: String,
    val photo: String,
    val points: Int,
    val points_in_pounds: Int,
    val points_to_egp: String,
    val region: Region,
    val type: String,
    val wallet_balance: Int,
    val weight: Int
)