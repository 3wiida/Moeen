package com.example.moeen.network.model.paymentMethodsResponse


import com.google.gson.annotations.SerializedName

data class PaymentMethodsResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("massage")
    val massage: String?,
    @SerializedName("status")
    val status: Int?
) {
    data class Data(
        @SerializedName("created_at")
        val createdAt: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("name_text")
        val nameText: String?,
        @SerializedName("updated_at")
        val updatedAt: String?,

        var isChecked:Boolean=false
    )
}