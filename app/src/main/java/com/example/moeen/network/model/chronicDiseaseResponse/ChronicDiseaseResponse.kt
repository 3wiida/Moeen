package com.example.moeen.network.model.chronicDiseaseResponse

data class ChronicDiseaseResponse(
    val data: List<Data>,
)

data class Data(
    val description: String,
    val id: Int,
    val name: String
)