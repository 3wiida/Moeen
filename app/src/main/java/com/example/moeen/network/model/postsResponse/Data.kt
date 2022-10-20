package com.example.moeen.network.model.postsResponse

data class Data(
    val id: Int,
    val is_active: Int,
    val long_description: String,
    val order: Int,
    val photo: String,
    val short_description: String,
    val slug: String,
    val title: String
)