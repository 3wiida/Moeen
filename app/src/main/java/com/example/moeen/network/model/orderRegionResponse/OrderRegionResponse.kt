package com.example.moeen.network.model.orderRegionResponse

data class OrderRegionResponse(
    val city_id: Int,
    val governorate_id: Int,
    val message: String,
    val region_id: Int,
    val status: Int
)