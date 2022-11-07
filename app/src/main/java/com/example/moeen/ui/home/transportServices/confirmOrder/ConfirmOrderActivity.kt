package com.example.moeen.ui.home.transportServices.confirmOrder

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.moeen.base.BaseActivity
import com.example.moeen.common.Constants.TAG
import com.example.moeen.databinding.ActivityConfirmOrderBinding
import com.example.moeen.ui.home.transportServices.ambulance.locationSelection.pojo.LocationAddress
import com.example.moeen.ui.home.transportServices.confirmOrder.pojo.OrderBody
import com.example.moeen.utils.resultWrapper.ApiResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmOrderActivity : BaseActivity() {
    /** vars */
    private lateinit var binding: ActivityConfirmOrderBinding
    private val viewModel: ConfirmOrderViewModel by viewModels()
    @Inject
    lateinit var bundle: Bundle

    //make order parameters
    private var carId: Int = 0
    private var govId: Int = -1
    private var govIdArrival = -1
    private var cityId = -1
    private var cityIdArrival = -1
    private var distance: Double = 0.0
    private var serviceId: Int = 0
    private var couponCode: String? = null
    private var paymentMethod: Int = 1
    private lateinit var movingLat: String
    private lateinit var movingLong: String
    private lateinit var arrivalLat: String
    private lateinit var arrivalLong: String
    private lateinit var movingAddressTitle: String
    private lateinit var arrivalAddressTitle: String
    private lateinit var tripDate: String

    /** ---------------------------------------------------------------------------------*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmOrderBinding.inflate(layoutInflater)
        receiveParametersFromBundle()
        setContentView(binding.root)

        if (checkInternetConnection()) {
            collectMakeOrderFlow()
        } else {
            showToast(this, "no internet connection")
        }

        binding.confirmOrderBtn.setOnClickListener {
            validateForm()
            Log.d(TAG, "onCreate: ${viewModel.formErrors}")
        }

    }

    /** check is form valid or not */
    private fun validateForm(): Boolean {
        val name = binding.etConfirmOrderPersonName.text.toString().trim()
        val phone = binding.etConfirmOrderPhoneNumber.text.toString().trim()
        return viewModel.validateForm(name, phone)
    }


    /** get make order required params from other screens */
    private fun receiveParametersFromBundle() {
        val movingLocation = bundle.getSerializable("movingLocation") as LocationAddress
        val arrivalLocation = bundle.getSerializable("arrivalLocation") as LocationAddress
        carId = bundle.getInt("carTypeId")
        govId = bundle.getInt("movingGovId")
        govIdArrival = bundle.getInt("arrivalGovId")
        cityId = bundle.getInt("movingCityId")
        cityIdArrival = bundle.getInt("arrivalCityId")
        distance = bundle.getDouble("distance")
        serviceId = bundle.getInt("serviceId")
        tripDate = bundle.getString("date")!!
        movingLat = movingLocation.lat
        movingLong = movingLocation.lon
        arrivalLat = arrivalLocation.lat
        arrivalLong = arrivalLocation.lon
        movingAddressTitle = movingLocation.addressLine
        arrivalAddressTitle = arrivalLocation.addressLine
    }

    /** collect Data Section, it's Danger */
    private fun collectMakeOrderFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.makeOrderResponse.collect { response ->
                    when (response) {
                        ApiResult.Empty -> {}
                        is ApiResult.Failure -> {
                            loadingDialog().cancel()
                            showToast(this@ConfirmOrderActivity, response.message!!)
                        }
                        ApiResult.Loading -> loadingDialog().show()
                        is ApiResult.Success<*> -> {
                            loadingDialog().cancel()
                            showToast(this@ConfirmOrderActivity, "Success")
                        }
                    }
                }
            }
        }
    }
}