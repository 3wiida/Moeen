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