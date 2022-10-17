package com.example.moeen.network.model

data class ErrorModel(
    val data: Map<String, List<String>>? = null,
    val massage: String? = null,
    val status: Int
)