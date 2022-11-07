package com.example.moeen.ui.home.transportServices.confirmOrder.pojo

data class OrderBody (
    val name:String="",
    val phone:String="",
    val governorate_id:Int=-1,
    val governorate_id_arrival:Int=-1,
    val city_id:Int=-1,
    val city_id_arrival:Int=-1,
    val distance:Double=0.0,
    val car_type_id:Int=-1,
    val service_id:Int=-1,
    val start_address_type:String="Location",
    val end_address_type:String="Location",
    val schedule_date:String="",
    val payment_method_id:Int=-1,
    val start_address_title:String="",
    val end_address_title:String="",
    val coupon_code:String?=null,
    val start_latitude:String,
    val start_longitude:String,
    val end_latitude:String,
    val end_longitude:String,
    val region_id:Int,
    val region_id_arrival:Int,
    val other_phone:String?=null
)