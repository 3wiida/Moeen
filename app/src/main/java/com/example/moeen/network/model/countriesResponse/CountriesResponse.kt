package com.example.moeen.network.model.countriesResponse

data class CountriesResponse(
    val data: List<Data>,
)

data class Data(
    val code: String,
    val id: Int,
    val name: String,
    val nationality: String
)