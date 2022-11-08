package com.example.moeen.network.api

import com.example.moeen.network.model.calculateDistanceResponse.CalculateDistanceResponse
import com.example.moeen.network.model.carsTypesResponse.CarsTypesResponse
import com.example.moeen.network.model.chronicDiseaseResponse.ChronicDiseaseResponse
import com.example.moeen.network.model.citiesResponse.CitiesResponse
import com.example.moeen.network.model.claculatePriceResponse.CalculatePriceResponse
import com.example.moeen.network.model.countriesResponse.CountriesResponse
import com.example.moeen.network.model.doctorsResponse.DoctorsResponse
import com.example.moeen.network.model.homeResponse.HomeResponse
import com.example.moeen.network.model.loginResponse.LoginResponse
import com.example.moeen.network.model.orderRegionResponse.OrderRegionResponse
import com.example.moeen.network.model.paymentMethodsResponse.PaymentMethodsResponse
import com.example.moeen.network.model.postsResponse.PostsResponse
import com.example.moeen.network.model.profileResponse.ProfileResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import com.example.moeen.network.model.regionsResponse.RegionsResponse
import com.example.moeen.network.model.specializationsResponse.SpecializationsResponse
import com.example.moeen.ui.home.transportServices.confirmOrder.pojo.OrderBody
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
        @Query("specialty_id") id : Int? = null,
        @Query("city_id") city : Int? = null,
        @Query("region_id") region : Int? = null,
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

    @FormUrlEncoded
    @POST("v1/make-orderv3")
    suspend fun makeTransportOrder(
        @Field("name") name:String,
        @Field("phone") phone: String,
        @Field("governorate_id") governorate_id:Int,
        @Field("governorate_id_arrival") governorate_id_arrival:Int,
        @Field("city_id") city_id:Int,
        @Field("city_id_arrival") city_id_arrival:Int,
        @Field("car_type_id") car_type_id:Int,
        @Field("service_id") service_id:Int,
        @Field("payment_method_id") payment_method_id:Int,
        @Field("distance") distance:Double,
        @Field("start_address_title") start_address_title:String,
        @Field("end_address_title") end_address_title:String,
        @Field("schedule_date") schedule_date:String,
        @Field("start_latitude") start_latitude:String,
        @Field("start_longitude") start_longitude:String,
        @Field("end_latitude") end_latitude:String,
        @Field("end_longitude") end_longitude:String,
        @Field("region_id") region_id:Int,
        @Field("region_id_arrival") region_id_arrival:Int,
        @Field("other_phone") other_phone:String?,
        @Field("coupon_code") coupon_code:String?
    )

    @GET("v1/calculate-distance")
    suspend fun calculateDistance(
        @Query("car_type_id") car_type_id:Int,
        @Query("start_latitude") start_latitude:String,
        @Query("start_longitude") start_longitude:String,
        @Query("end_latitude") end_latitude:String,
        @Query("end_longitude") end_longitude:String
    ):CalculateDistanceResponse

    @GET("v1/payment-methods")
    suspend fun getPaymentMethods() : PaymentMethodsResponse

    @FormUrlEncoded
    @POST("v1/check-coupon")
    suspend fun checkCoupon(
        @Field("coupon_code") coupon_code:String,
        @Field("price") price:Float
    )
}
