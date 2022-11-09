package com.example.moeen.network.model.couponResponse


import com.google.gson.annotations.SerializedName

data class CouponResponse(
    @SerializedName("data")
    val data: Data?,
    @SerializedName("massage")
    val massage: String?,
    @SerializedName("status")
    val status: Int?
) {
    data class Data(
        @SerializedName("coupon")
        val coupon: Coupon?,
        @SerializedName("discount")
        val discount: Float?,
        @SerializedName("price")
        val price: Float?,
        @SerializedName("total_price")
        val totalPrice: Float?
    ) {
        data class Coupon(
            @SerializedName("amount")
            val amount: Int?,
            @SerializedName("code")
            val code: String?,
            @SerializedName("created_at")
            val createdAt: String?,
            @SerializedName("cut_from")
            val cutFrom: String?,
            @SerializedName("expired_date")
            val expiredDate: String?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("is_active")
            val isActive: Int?,
            @SerializedName("max_discount")
            val maxDiscount: String?,
            @SerializedName("max_one_client_use")
            val maxOneClientUse: Int?,
            @SerializedName("min_discount")
            val minDiscount: String?,
            @SerializedName("start_date")
            val startDate: String?,
            @SerializedName("type")
            val type: String?,
            @SerializedName("updated_at")
            val updatedAt: String?,
            @SerializedName("use_frequency")
            val useFrequency: Int?
        )
    }
}