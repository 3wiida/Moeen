package com.example.moeen.network.model.homeResponse

data class Data(
    val description: String,
    val id: Int,
    val name: String,
    val services: List<Service>,
    val type: String
)