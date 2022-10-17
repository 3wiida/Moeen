package com.example.moeen.utils.resultWrapper

import com.example.moeen.network.model.ErrorModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

sealed class ResultWrapper <T> {
    data class Success<T>(val results: T) : ResultWrapper<T>()
    data class Failure(val code: Int? = null, val message: String? = null) : ResultWrapper<Nothing>()
    object Loading : ResultWrapper<Nothing>()
}

fun <T> performSafeApiCall(apiCall:suspend () -> T) = flow {
    emit(ResultWrapper.Loading)
    try {
        emit(ResultWrapper.Success(apiCall.invoke()))
    }catch (throwable:Throwable){
        when(throwable){
            is IOException-> emit(ResultWrapper.Failure(null,"Please check your internet connection"))
            is HttpException -> emit(ResultWrapper.Failure(throwable.code(), message = convertErrorBody(throwable)))
        }
    }
}



private fun convertErrorBody(e:HttpException):String{
    var error= Gson().fromJson(e.response()?.errorBody()?.string(), ErrorModel::class.java)
    return if(error.data==null){
        error.massage!!
    }else{
        var errorText=error.data?.values!!.toList()[0].toString()
        errorText.slice(1..errorText.length-2)
    }
}
