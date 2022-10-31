package com.example.moeen.network.model.specializationsResponse


import com.google.gson.annotations.SerializedName

data class SpecializationsResponse(
    @SerializedName("data")
    val `data`: MutableList<Data>,
    @SerializedName("massage")
    val massage: String,
    @SerializedName("status")
    val status: Int
) {
    data class Data(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("photo")
        val photo: String,
        var checked : Boolean = false
    )
}