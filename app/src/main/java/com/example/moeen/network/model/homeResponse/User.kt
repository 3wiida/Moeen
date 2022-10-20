package com.example.moeen.network.model.homeResponse

data class User(
    val accept: Int,
    val age: Int,
    val blood_type: Any,
    val chronic_diseases: List<Any>,
    val country_id: Int,
    val d_o_b: Any,
    val gender: Any,
    val gender_text: Any,
    val hospital_client: Any,
    val id: Int,
    val insurance_number: Any,
    val length: Any,
    val name: Any,
    val national_id: Any,
    val nationality: Any,
    val notes: Any,
    val phone: String,
    val photo: String,
    val points: Int,
    val points_in_pounds: Int,
    val points_to_egp: String,
    val region: Any,
    val type: String,
    val wallet_balance: Int,
    val weight: Any
)