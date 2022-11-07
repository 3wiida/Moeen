package com.example.moeen.network.model.doctorsResponse


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DoctorsResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("degress")
    val degress: Degress?,
    @SerializedName("links")
    val links: Links?,
    @SerializedName("massage")
    val massage: String?,
    @SerializedName("meta")
    val meta: Meta?,
    @SerializedName("status")
    val status: Int?
) {
    data class Data(
        @SerializedName("address")
        val address: String?,
        @SerializedName("auto_message")
        val autoMessage: String?,
        @SerializedName("city_id")
        val cityId: Int?,
        @SerializedName("d_o_b")
        val dOB: Any?,
        @SerializedName("degree")
        val degree: String?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("gender")
        val gender: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("medical_price")
        val medicalPrice: Int?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("nationality")
        val nationality: Any?,
        @SerializedName("phone")
        val phone: String?,
        @SerializedName("photo")
        val photo: String?,
        @SerializedName("rate")
        val rate: String?,
        @SerializedName("region")
        val region: Region?,
        @SerializedName("specialty_id")
        val specialtyId: SpecialtyId?,
        @SerializedName("status")
        val status: Any?,
        @SerializedName("transport_photo")
        val transportPhoto: Any?,
        @SerializedName("user_name")
        val userName: Any?,
        @SerializedName("visit_price")
        val visitPrice: Int?,
        @SerializedName("work_times")
        val workTimes: List<WorkTime?>?
    ) : Serializable {
        data class Region(
            @SerializedName("city")
            val city: City?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("name")
            val name: String?
        ) {
            data class City(
                @SerializedName("id")
                val id: Int?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("state")
                val state: State?
            ) {
                data class State(
                    @SerializedName("country")
                    val country: Country?,
                    @SerializedName("id")
                    val id: Int?,
                    @SerializedName("name")
                    val name: String?
                ) {
                    data class Country(
                        @SerializedName("code")
                        val code: String?,
                        @SerializedName("id")
                        val id: Int?,
                        @SerializedName("name")
                        val name: String?,
                        @SerializedName("nationality")
                        val nationality: String?
                    )
                }
            }
        }

        data class SpecialtyId(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("photo")
            val photo: String?
        )

        data class WorkTime(
            @SerializedName("date")
            val date: String?,
            @SerializedName("day")
            val day: String?,
            @SerializedName("shifts")
            val shifts: List<Shift?>?
        ) {
            data class Shift(
                @SerializedName("day")
                val day: String?,
                @SerializedName("end")
                val end: String?,
                @SerializedName("id")
                val id: Int?,
                @SerializedName("medical_times")
                val medicalTimes: Int?,
                @SerializedName("remaing_times")
                val remaingTimes: Int?,
                @SerializedName("start")
                val start: String?,
                var clicked: Boolean = false
            )
        }
    }

    data class Degress(
        @SerializedName("consultant")
        val consultant: String?,
        @SerializedName("professor")
        val professor: String?,
        @SerializedName("professor-first")
        val professorFirst: String?,
        @SerializedName("resident")
        val resident: String?,
        @SerializedName("specialist")
        val specialist: String?,
        @SerializedName("specialist-first")
        val specialistFirst: String?,
        @SerializedName("trainee")
        val trainee: String?
    )

    data class Links(
        @SerializedName("first")
        val first: String?,
        @SerializedName("last")
        val last: String?,
        @SerializedName("next")
        val next: Any?,
        @SerializedName("prev")
        val prev: Any?
    )

    data class Meta(
        @SerializedName("current_page")
        val currentPage: Int?,
        @SerializedName("from")
        val from: Int?,
        @SerializedName("last_page")
        val lastPage: Int?,
        @SerializedName("path")
        val path: String?,
        @SerializedName("per_page")
        val perPage: Int?,
        @SerializedName("to")
        val to: Int?,
        @SerializedName("total")
        val total: Int?
    )
}