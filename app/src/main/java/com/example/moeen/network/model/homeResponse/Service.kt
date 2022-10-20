package com.example.moeen.network.model.homeResponse

data class Service(
    val cost_range: String,
    val cost_unit: String,
    val delivery_time_unit: String,
    val id: Int,
    val is_active: Int,
    val is_open: Int,
    val level: Int,
    val name: String,
    val photo: String,
    val service_type: String,
    val time: Int,
    val type: String
)