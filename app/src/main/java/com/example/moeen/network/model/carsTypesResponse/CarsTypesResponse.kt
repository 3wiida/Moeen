package com.example.moeen.network.model.carsTypesResponse

data class CarsTypesResponse(
    val data: List<Data>,
)

data class Data(
    val id: Int,
    val km_price: String,
    val minute_price: String,
    val name: String
)