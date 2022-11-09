package com.example.moeen.ui.home.transportServices.confirmOrder

import com.example.moeen.network.api.ApiServices
import com.example.moeen.ui.home.transportServices.confirmOrder.pojo.OrderBody
import com.example.moeen.utils.resultWrapper.performSafeApiCall
import javax.inject.Inject

class ConfirmOrderRepository @Inject constructor(private val apiServices: ApiServices) {
    suspend fun makeTransportOrder(orderBody: OrderBody) = performSafeApiCall {
        apiServices.makeTransportOrder(
            orderBody.name,
            orderBody.phone,
            orderBody.governorate_id,
            orderBody.governorate_id_arrival,
            orderBody.city_id,
            orderBody.city_id_arrival,
            orderBody.car_type_id,
            orderBody.service_id,
            orderBody.payment_method_id,
            orderBody.distance,
            orderBody.start_address_title,
            orderBody.end_address_title,
            orderBody.schedule_date,
            orderBody.start_latitude,
            orderBody.start_longitude,
            orderBody.end_latitude,
            orderBody.end_longitude,
            orderBody.region_id,
            orderBody.region_id_arrival,
            orderBody.other_phone,
            orderBody.coupon_code
        )
    }

    suspend fun checkCoupon(code:String,price:Float)= performSafeApiCall { apiServices.checkCoupon(code,price) }

    suspend fun getPaymentMethods()= performSafeApiCall { apiServices.getPaymentMethods() }
}