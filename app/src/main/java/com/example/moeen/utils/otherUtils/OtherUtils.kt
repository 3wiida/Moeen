package com.example.moeen.utils

import android.content.Context
import kotlin.math.roundToInt

fun removePhoneFirstZero(countryCode:String,phoneNumber:String):String{
    return if(countryCode[countryCode.length-1] == '0' && phoneNumber[0]== '0'){
        val newPhoneNumber=phoneNumber.slice(1 until phoneNumber.length)
        "+$countryCode$newPhoneNumber"
    }else{
        "+$countryCode$phoneNumber"
    }
}

fun dpToPx(dp : Int, context : Context): Int {
    val density = context.resources.displayMetrics.density;
    return (dp * density).roundToInt()
}

enum class FormErrors{
    INVALID_PHONE,
    INVALID_NAME,
    INVALID_PASSWORD,
    INVALID_CONFIRM_PASSWORD,
    INVALID_NEW_PASSWORD,
    INVALID_MATCH_PASSWORD,
    INVALID_EMAIL,
    INVALID_BIRTH_DATE,
    INVALID_GENDER,
    INVALID_SPECIALITY,
    INVALID_COMPANY,
    INVALID_HELPER,
    INVALID_ADDRESS,
    INVALID_NATIONALITY_ID,
    INVALID_LENGTH,
    INVALID_WEIGHT,
    EMPTY_MOVING_PLACE,
    EMPTY_ARRIVAL_PLACE,
    EMPTY_CAR_TYPE
}