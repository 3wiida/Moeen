package com.example.moeen.ui.home.transportServices.confirmOrder

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.ObservableFloat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moeen.R
import com.example.moeen.base.BaseActivity
import com.example.moeen.common.Constants.TAG
import com.example.moeen.databinding.ActivityConfirmOrderBinding
import com.example.moeen.network.model.couponResponse.CouponResponse
import com.example.moeen.network.model.paymentMethodsResponse.PaymentMethodsResponse
import com.example.moeen.ui.home.HomeActivity
import com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments.PaymentMethodsAdapter
import com.example.moeen.ui.home.transportServices.locationSelection.pojo.LocationAddress
import com.example.moeen.ui.home.transportServices.confirmOrder.pojo.OrderBody
import com.example.moeen.utils.otherUtils.FormErrors
import com.example.moeen.utils.resultWrapper.ApiResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
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
    private var paymentMethodId: Int = 1
    private var movingRegionId:Int=1
    private var arrivalRegionId:Int=1
    private lateinit var name:String
    private lateinit var phone:String
    private lateinit var otherPhone:String
    private lateinit var movingLat: String
    private lateinit var movingLong: String
    private lateinit var arrivalLat: String
    private lateinit var arrivalLong: String
    private lateinit var movingAddressTitle: String
    private lateinit var arrivalAddressTitle: String
    private lateinit var tripDate: String
    private val ad=PaymentMethodsAdapter(R.layout.payment_method_rv_line)

    //prices
    var tripCost=ObservableFloat()
    //var tax=ObservableFloat()
    var discount=ObservableFloat()
    var total=ObservableFloat()

    //flows
    private  var makeOrderJob:Job?=null
    private  var checkCouponJob:Job?=null

    /** ---------------------------------------------------------------------------------*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmOrderBinding.inflate(layoutInflater)
        binding.viewModel=viewModel
        binding.activity=this
        receiveParametersFromBundle()
        ad.onItemClicked={  paymentMethod ->
            paymentMethod.id?.let {
                paymentMethodId=it
                Log.d(TAG, "initRv: $it")
            }
        }
        total.set(tripCost.get()-discount.get())
        setContentView(binding.root)
        if (checkInternetConnection()) {
            viewModel.getPaymentMethods()
            collectMakeOrderFlow()
            collectCheckOrderFlow()
            collectPaymentMethodsFlow()
        } else {
            showToast(this, "no internet connection")
        }

        binding.confirmOrderBtn.setOnClickListener {
            if(validateForm() && checkInternetConnection()){
                name=binding.etConfirmOrderPersonName.text.toString().trim()
                phone=binding.etConfirmOrderPhoneNumber.text.toString().trim()
                otherPhone=binding.etConfirmOrderAnotherPhoneNumber.text.toString().trim()
                couponCode=binding.etConfirmOrderDiscountCode.text.toString().trim()
                val order = OrderBody(
                    name,
                    phone,
                    govId,
                    govIdArrival,
                    cityId,
                    cityIdArrival,
                    distance,
                    carId,
                    serviceId,
                    tripDate,
                    paymentMethodId,
                    movingAddressTitle,
                    arrivalAddressTitle,
                    couponCode,
                    movingLat,
                    movingLong,
                    arrivalLat,
                    arrivalLong,
                    movingRegionId,
                    arrivalRegionId,
                    otherPhone
                )
                viewModel.makeOrder(order)
            }
        }


        binding.backBtn.setOnClickListener{
            finish()
        }


        binding.btnConfirmOrderCheckDiscount.setOnClickListener {
            couponCode=binding.etConfirmOrderDiscountCode.text.toString().trim()
            if(validateCoupon(couponCode)){
                viewModel.checkCoupon(couponCode!!,tripCost.get())
            }
        }
    }

    /** check is form valid or not */
    private fun validateForm(): Boolean {
        val name = binding.etConfirmOrderPersonName.text.toString().trim()
        val phone = binding.etConfirmOrderPhoneNumber.text.toString().trim()
        return viewModel.validateForm(name, phone)
    }

    private fun validateCoupon(couponCode:String?):Boolean{
        return viewModel.validateCoupon(couponCode)
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
        movingRegionId=bundle.getInt("movingRegionId")
        arrivalRegionId=bundle.getInt("arrivalRegionId")
        tripCost.set(bundle.getFloat("tripPrice"))
    }

    /** collect Data Section, DangerArea!!! */
    private fun collectMakeOrderFlow() {
         makeOrderJob=lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.makeOrderResponse.collect { response ->
                    when (response) {
                        ApiResult.Empty -> {}
                        is ApiResult.Failure -> {
                            loadingDialog().cancel()
                            response.message?.let { showToast(this@ConfirmOrderActivity, it) }
                        }
                        ApiResult.Loading -> loadingDialog().show()
                        is ApiResult.Success<*> -> {
                            loadingDialog().cancel()
                            showThanksDialog()
                        }
                    }
                }
            }
        }
    }

    private fun collectCheckOrderFlow(){
        checkCouponJob=lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.checkCouponResponse.collectLatest { result->
                    when(result){
                        ApiResult.Empty -> {}
                        is ApiResult.Failure -> {
                            loadingDialog().dismiss()
                            showToast(this@ConfirmOrderActivity,result.message!!)
                        }
                        ApiResult.Loading -> loadingDialog().show()
                        is ApiResult.Success<*> -> {
                            loadingDialog().dismiss()
                            val couponResult = result.data as CouponResponse
                            if(couponResult.status==0){
                                Log.d(TAG, "collectCheckOrderFlow: enter state 0")
                                viewModel.couponError.add(FormErrors.INVALID_COUPON_CODE)
                                couponResult.massage?.let { message ->

                                    showToast(this@ConfirmOrderActivity,message)
                                }
                            }else{
                                Log.d(TAG, "collectCheckOrderFlow: enter state 1")
                                couponResult.massage?.let { message ->
                                    showToast(this@ConfirmOrderActivity,message)
                                    couponResult.data?.discount?.let { discount.set(it) }
                                    couponResult.data?.totalPrice?.let { total.set(it) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun collectPaymentMethodsFlow(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.paymentMethodsResponse.collectLatest { result->
                    when(result){
                        ApiResult.Empty -> {}
                        is ApiResult.Failure -> {
                            loadingDialog().dismiss()
                            result.message?.let { showToast(this@ConfirmOrderActivity, it) }
                        }
                        ApiResult.Loading -> loadingDialog().show()
                        is ApiResult.Success<*> -> {
                            loadingDialog().dismiss()
                            val paymentMethods=result.data as PaymentMethodsResponse
                            if(paymentMethods.status==0){
                                paymentMethods.massage?.let {
                                    showToast(this@ConfirmOrderActivity,
                                        it
                                    )
                                }
                            }else{
                                ad.submitList(paymentMethods.data)
                                initRv()
                            }
                        }
                    }
                }
            }
        }
    }

    /** thank you dialog when user confirm order */
    private fun showThanksDialog(){
        val dialog=Dialog(this)
        dialog.apply {
            setContentView(R.layout.complete_order_dialog_layout)
            window?.decorView?.setBackgroundResource(R.drawable._15_white_rect)
            window?.setLayout(900,ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.attributes?.windowAnimations= R.style.loadingDialogAnimation
        }
        dialog.setOnCancelListener {
            val intent=Intent(this,HomeActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        dialog.show()
    }


    private fun initRv(){
        binding.paymentMethodRv.apply {
            adapter=ad
            layoutManager=GridLayoutManager(this@ConfirmOrderActivity,2)
        }
    }





    override fun onDestroy() {

        makeOrderJob?.cancel()

        checkCouponJob?.cancel()

        super.onDestroy()
    }
}