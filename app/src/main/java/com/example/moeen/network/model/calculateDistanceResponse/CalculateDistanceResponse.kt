package com.example.moeen.network.model.calculateDistanceResponse

data class CalculateDistanceResponse(
    val data: Data,
    val massage: String,
    val status: Int
)

data class Data(
    val distance: Double,
    val total_price: Double
)