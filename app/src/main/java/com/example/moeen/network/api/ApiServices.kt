package com.example.moeen.network.api

import com.example.moeen.network.model.countriesResponse.CountriesResponse
import com.example.moeen.network.model.loginResponse.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServices {

    @FormUrlEncoded
    @POST("v1/login-v2")
    suspend fun loginUser(
        @Field("phone") phone:String,
        @Field("country") country:String,
        @Field("token") token:String,
        @Field("os") os:String="android"
    ):LoginResponse

    @GET("v1/countries")
    suspend fun getCountries():CountriesResponse
}