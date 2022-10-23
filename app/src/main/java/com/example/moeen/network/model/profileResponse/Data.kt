package com.example.moeen.network.model.profileResponse

data class Data(
    val blood_types: List<BloodType>,
    val countries: List<Country>,
    val diseases: List<Disease>,
    val regions: List<Region>,
    val token: String,
    val user: User
)