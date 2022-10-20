package com.example.moeen.utils

fun removePhoneFirstZero(countryCode:String,phoneNumber:String):String{
    return if(countryCode[countryCode.length-1] == '0' && phoneNumber[0]== '0'){
        val newPhoneNumber=phoneNumber.slice(1 until phoneNumber.length)
        "+$countryCode$newPhoneNumber"
    }else{
        "+$countryCode$phoneNumber"
    }
}