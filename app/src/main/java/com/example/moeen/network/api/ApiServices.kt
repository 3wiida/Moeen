package com.example.moeen.network.api

import com.example.moeen.network.model.countriesResponse.CountriesResponse
import com.example.moeen.network.model.homeResponse.HomeResponse
import com.example.moeen.network.model.loginResponse.LoginResponse
import com.example.moeen.network.model.postsResponse.PostsResponse
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

    @GET("v1/home")
    suspend fun getHome() : HomeResponse

    @GET("v1/posts")
    suspend fun getPosts() : PostsResponse
}