package com.example.moeen.network.model.regionsResponse


import com.google.gson.annotations.SerializedName

data class RegionsResponse(
    @SerializedName("data")
    val `data`: MutableList<Data>,
    @SerializedName("massage")
    val massage: String,
    @SerializedName("status")
    val status: Int
) {
    data class Data(
        @SerializedName("city")
        val city: City? = null,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
    ) {
        data class City(
            @SerializedName("id")
            val id: Int,
            @SerializedName("name")
            val name: String,
            @SerializedName("state")
            val state: State
        ) {
            data class State(
                @SerializedName("country")
                val country: Country,
                @SerializedName("id")
                val id: Int,
                @SerializedName("name")
                val name: String
            ) {
                data class Country(
                    @SerializedName("code")
                    val code: String,
                    @SerializedName("id")
                    val id: Int,
                    @SerializedName("name")
                    val name: String,
                    @SerializedName("nationality")
                    val nationality: String
                )
            }
        }
    }
}