package com.example.moeen.network.model.claculatePriceResponse

data class CalculatePriceResponse(
    val data: Data,
    val massage: String,
    val status: Int
)

data class Data(
    val price: Int
)