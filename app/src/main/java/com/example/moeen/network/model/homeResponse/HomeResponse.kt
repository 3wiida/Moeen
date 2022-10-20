package com.example.moeen.network.model.homeResponse

data class HomeResponse(
    val `data`: List<Data>,
    val massage: String,
    val sliders: List<Slider>,
    val status: Int,
    val user: User
)