package com.example.moeen.network.api

import com.example.moeen.network.model.carsTypesResponse.CarsTypesResponse
import com.example.moeen.network.model.chronicDiseaseResponse.ChronicDiseaseResponse
import com.example.moeen.network.model.citiesResponse.CitiesResponse
import com.example.moeen.network.model.claculatePriceResponse.CalculatePriceResponse
import com.example.moeen.network.model.countriesResponse.CountriesResponse
import com.example.moeen.network.model.doctorsResponse.DoctorsResponse
import com.example.moeen.network.model.homeResponse.HomeResponse
import com.example.moeen.network.model.loginResponse.LoginResponse
import com.example.moeen.network.model.orderRegionResponse.OrderRegionResponse
import com.example.moeen.network.model.postsResponse.PostsResponse
import com.example.moeen.network.model.profileResponse.ProfileResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import com.example.moeen.network.model.regionsResponse.RegionsResponse
import com.example.moeen.network.model.specializationsResponse.SpecializationsResponse
import retrofit2.http.*

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

    @GET("v1/client-profile")
    suspend fun getProfile():ProfileResponse

    @GET("v1/chronic-diseases")
    suspend fun getChronicDiseases():ChronicDiseaseResponse

    @GET("v1/car-types")
    suspend fun getCarTypes():CarsTypesResponse

    @FormUrlEncoded
    @POST("v1/update-profile")
    suspend fun updateProfile(
        @Field("region_id") region_id:Int,
        @Field("blood_type") blood_type:String,
        @Field("name") name:String,
        @Field("gender") gender:String,
        @Field("photo") photo: String?=null,
        @Field("national_id") national_id: String,
        @Field("nationality") nationality: String,
        @Field("weight") weight: Int,
        @Field("length") length: Int,
        @Field("insurance_number") insurance_number: String,
        @Field("notes") notes:String,
        @Field("d_o_b") d_o_b:String
    )

    @GET("v1/specialties")
    suspend fun getSpecializations() : SpecializationsResponse

    @GET("v1/doctors")
    suspend fun getDoctors(
        @Query("specialty_id") id : Int? = null
    ) : DoctorsResponse

    @GET("v1/cities")
    suspend fun getCities() : CitiesResponse

    @GET("v1/regions")
    suspend fun getRegions(
        @Query("city_id") cityId : Int?
    ) : RegionsResponse

    @FormUrlEncoded
    @POST("v1/calculate-price")
    suspend fun calculatePrice(
        @Field("governorate_id") governorate_id:Int,
        @Field("governorate_id_arrival") governorate_id_arrival:Int,
        @Field("city_id") city_id:Int,
        @Field("city_id_arrival") city_id_arrival:Int,
        @Field("car_type_id") car_type_id:Int
    ):CalculatePriceResponse

    @FormUrlEncoded
    @POST("v1/check-order-region")
    suspend fun checkOrderRegion(
        @Field("latitude") latitude:String,
        @Field("longitude") longitude:String
    ):OrderRegionResponse
}
