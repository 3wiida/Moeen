package com.example.moeen.ui.Login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.moeen.R
import com.example.moeen.base.BaseFragment
import com.example.moeen.common.Constants.TAG
import com.example.moeen.databinding.FragmentOtpBinding
import com.example.moeen.ui.Login.LoginViewModel.Companion.code
import com.example.moeen.ui.Login.LoginViewModel.Companion.resendToken
import com.example.moeen.ui.home.HomeActivity
import com.example.moeen.utils.resultWrapper.ApiResult
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OtpFragment : BaseFragment() {
    @Inject lateinit var bundle: Bundle
    lateinit var binding: FragmentOtpBinding
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_otp, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //TODO start timer for resend code -----------------------------------------------------------------
        viewModel.startResendOtpTimer()
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.otpTimer.collect {
                binding.resendOtpTimer.text = " ( 0:$it )"
                if (it == 0) {
                    binding.resendOtpTv.setTextColor(Color.parseColor("#01B7CD"))
                    binding.resendOtpTv.isEnabled = true
                    binding.resendOtpTimer.visibility = View.GONE
                }
            }
        }

        //TODO resend code click handle-------------------------------------------------------------------
        binding.resendOtpTv.setOnClickListener {
            loadingDialog().show()
            viewModel.startResendOtpTimer()
            binding.resendOtpTv.isEnabled = false
            binding.resendOtpTv.setTextColor(Color.parseColor("#8F9596"))
            binding.resendOtpTimer.visibility = View.VISIBLE
            viewModel.resendOtpCode(activity as Activity,bundle.getString("phoneNumber")!!,resendToken)
            loadingDialog().dismiss()
        }



        //TODO back btn click handle----------------------------------------------------------------------
        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }



        //TODO next btn click handle----------------------------------------------------------------------
        binding.otpNextBtn.setOnClickListener {

            if(binding.pinView.text!!.isEmpty() || code==null){
                binding.wrongOtpErrorMessageTv.visibility = View.VISIBLE
            }else{
                Log.d(TAG, "onViewCreated: $code")
                val credential = PhoneAuthProvider.getCredential(code,binding.pinView.text.toString())
                viewModel.signInWithCredential(credential)

                viewModel.sendCodeState.observe(viewLifecycleOwner) {
                    if (it == "success") {
                        binding.wrongOtpErrorMessageTv.visibility = View.GONE
                        //TODO fire login call------------------------
                        viewModel.login(bundle.getString("phoneNumber").toString(),bundle.getString("countryName").toString(),"123")
                        lifecycleScope.launch {
                            viewModel.apiState.collect{ apiResult ->
                                when(apiResult){
                                    is ApiResult.Failure -> {
                                        loadingDialog().cancel()
                                        showToast(requireContext(),R.string.unknowError.toString())
                                    }
                                    ApiResult.Loading -> loadingDialog().show()
                                    is ApiResult.Success<*> -> {
                                        loadingDialog().cancel()
                                        startActivity(Intent(activity, HomeActivity::class.java))
                                        activity?.finish()
                                    }
                                }
                            }
                        }
                    } else {
                        binding.wrongOtpErrorMessageTv.visibility = View.VISIBLE
                    }
                }
                loadingDialog().dismiss()
            }
        }
    }
}

