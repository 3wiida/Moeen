package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.compose.runtime.DisposableEffect
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moeen.R
import com.example.moeen.base.BaseFragment
import com.example.moeen.databinding.FragmentConfirmationAndPaymentBinding
import com.example.moeen.network.model.paymentMethodsResponse.PaymentMethodsResponse
import com.example.moeen.ui.home.medicalServices.doctorsBooking.DoctorsBookingViewModel
import com.example.moeen.utils.resultWrapper.ApiResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ConfirmationAndPaymentFragment : BaseFragment() {

    private lateinit var binding: FragmentConfirmationAndPaymentBinding
    private val args : ConfirmationAndPaymentFragmentArgs by navArgs()
    private val viewModel : DoctorsBookingViewModel by viewModels()
    private val paymentMethodsAdapter = PaymentMethodsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirmation_and_payment, container, false)

        viewModel.getPaymentMethods()

        lifecycleScope.launch(Dispatchers.IO){
            viewModel.paymentState.collect{ state ->
                when(state){
                    ApiResult.Empty -> {}
                    ApiResult.Loading -> withContext(Dispatchers.Main){
                        loadingDialog().show()
                    }
                    is ApiResult.Failure -> withContext(Dispatchers.Main){
                        loadingDialog().cancel()
                        showToast(requireContext(), state.message.toString())
                    }
                    is ApiResult.Success<*> -> withContext(Dispatchers.Main){
                        loadingDialog().cancel()
                        val result = (state.data as PaymentMethodsResponse).data
                        paymentMethodsAdapter.submitList(result)
                        binding.rvPaymentMethods.adapter = paymentMethodsAdapter
                        binding.rvPaymentMethods.layoutManager = LinearLayoutManager(requireContext())
                    }
                }
            }
        }

        binding.btnConfirmPayment.setOnClickListener {
            view?.findNavController()?.navigate(ConfirmationAndPaymentFragmentDirections
                .actionConfirmationAndPaymentFragmentToConfirmationDataFragment(args.doctorName, args.doctorSpeciality, args.day, args.startTime, args.endTime))
        }


        return binding.root
    }
}