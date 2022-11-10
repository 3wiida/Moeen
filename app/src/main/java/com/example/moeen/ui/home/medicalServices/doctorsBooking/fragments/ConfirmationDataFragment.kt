package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.moeen.R
import com.example.moeen.base.BaseFragment
import com.example.moeen.databinding.FragmentConfirmationDataBinding
import com.example.moeen.network.model.couponResponse.CouponResponse
import com.example.moeen.ui.home.medicalServices.doctorsBooking.DoctorsBookingViewModel
import com.example.moeen.utils.resultWrapper.ApiResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ConfirmationDataFragment : BaseFragment() {

    private lateinit var binding: FragmentConfirmationDataBinding
    private val args : ConfirmationDataFragmentArgs by navArgs()
    private val viewModel : DoctorsBookingViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirmation_data, container, false)

        binding.include.tvFragmentTitle.text = "تأكيد البيانات"
        binding.include.ivBackArrow.setOnClickListener {
            view?.findNavController()?.navigateUp()
        }

        /** The Doctor Static Data */
        binding.etDoctorName.setText(args.doctorName)
        binding.etDoctorSpeciality.setText(args.doctorSpeciality)
        val text = "${args.day}, from ${args.startTime.slice(0..args.startTime.length-4)} to ${args.endTime.slice(0..args.endTime.length-4)}"
        binding.etReserveTime.setText(text)
        binding.tvSessionPrice.text = args.sessionPrice.toString()
        binding.tvTotalPrice.text = args.sessionPrice.toString()


        // TODO: Check the coupon, texes, then add them to the total
        binding.btnCheckDiscountConfirmData.setOnClickListener {
            val coupon = binding.etDiscountConfirmData.text.toString()
            viewModel.checkCoupon(coupon, args.sessionPrice.toFloat())

            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.couponState.collect { state ->
                    when (state) {
                        ApiResult.Empty -> {}
                        ApiResult.Loading -> withContext(Dispatchers.Main) {
                            loadingDialog().show()
                        }
                        is ApiResult.Failure -> withContext(Dispatchers.Main){
                            loadingDialog().cancel()
                            showToast(requireContext(), "Please Enter a valid coupon!")
                        }
                        is ApiResult.Success<*> -> withContext(Dispatchers.Main){
                            loadingDialog().cancel()
                            binding.llDiscount.visibility = View.VISIBLE
                            binding.tvDiscountPrice.text = (state.data as CouponResponse).data!!.discount.toString()
                            val totalPrice = args.sessionPrice + binding.tvSessionPrice.text.toString().toFloat()
                            binding.tvTotalPrice.text = totalPrice.toString()
                        }
                    }
                }
            }
        }


        // TODO: Check the call
        binding.btnConfirmData.setOnClickListener {
            showToast(requireContext(), "Done")
            Handler(Looper.getMainLooper()).postDelayed({
                activity?.finish()
            }, 1000L)
        }


        return binding.root
    }
}