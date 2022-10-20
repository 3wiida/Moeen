package com.example.moeen.utils.resultWrapper

import com.example.moeen.network.model.ErrorModel
import com.google.android.gms.common.api.Api
import com.google.gson.Gson
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

sealed class ResultWrapper <out T> {
    data class Success<T>(val results: T) : ResultWrapper<T>()
    data class Failure(val code: Int? = null, val message: String? = null) : ResultWrapper<Nothing>()
}

suspend fun <T> performSafeApiCall(apiCall:suspend () -> T) :ResultWrapper<T>{
    return try {
        ResultWrapper.Success(apiCall.invoke())
    }catch (throwable:Throwable){
        when(throwable){
            is IOException -> ResultWrapper.Failure(message = "من فضلك تأكد من اتصالك بالانترنت")
            is HttpException -> ResultWrapper.Failure(throwable.code(), convertErrorBody(throwable))
            else ->ResultWrapper.Failure(message = throwable.message)
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

sealed class ApiResult{
    object Empty:ApiResult()
    object Loading:ApiResult()
    data class Success <T> (val data:T):ApiResult()
    data class Failure(val code:Int?=null,val message: String?=null):ApiResult()
}
