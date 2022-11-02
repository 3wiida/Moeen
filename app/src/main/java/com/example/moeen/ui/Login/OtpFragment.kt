package com.example.moeen.ui.Login

import android.annotation.SuppressLint
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
import com.example.moeen.ui.home.HomeActivity
import com.example.moeen.utils.resultWrapper.ApiResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OtpFragment : BaseFragment() {
    lateinit var binding: FragmentOtpBinding
    private val viewModel: LoginViewModel by viewModels()

    // args params
    lateinit var phone: String
    lateinit var country: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_otp, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //to get args from prev fragment on navigate
        getArgs()

        //TODO start timer for resend code -----------------------------------------------------------------
        lifecycleScope.launch{
            viewModel.startResendOtpTimer().collect{
                binding.resendOtpTimer.text = " ( 0:" + (if(it<10)"0$it )" else "$it )")
                if(it==0){
                    binding.resendOtpTv.setTextColor(Color.parseColor("#01B7CD"))
                    binding.resendOtpTv.isEnabled = true
                    binding.resendOtpTimer.visibility = View.GONE
                }
            }
        }

        //TODO resend code click handle-------------------------------------------------------------------
        binding.resendOtpTv.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                viewModel.startResendOtpTimer().collect{
                    binding.resendOtpTimer.text = " ( 0:" + (if(it<10)"0$it )" else "$it )")
                    if(it==0){
                        binding.resendOtpTv.setTextColor(Color.parseColor("#01B7CD"))
                        binding.resendOtpTv.isEnabled = true
                        binding.resendOtpTimer.visibility = View.GONE
                    }
                }
            }
            binding.resendOtpTv.isEnabled = false
            binding.resendOtpTv.setTextColor(Color.parseColor("#8F9596"))
            binding.resendOtpTimer.visibility = View.VISIBLE
            viewModel.resendCode(phone,requireActivity())
        }


        //TODO back btn click handle----------------------------------------------------------------------
        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }


        //TODO next btn click handle----------------------------------------------------------------------
        binding.otpNextBtn.setOnClickListener {
            viewModel.verifyCode(binding.pinView.text.toString(), requireActivity())
            lifecycleScope.launch {
                viewModel.getOtpStateFlow().collect {
                    when (it) {
                        ApiResult.Empty -> {}
                        is ApiResult.Failure -> {
                            loadingDialog().dismiss()
                            binding.wrongOtpErrorMessageTv.visibility=View.VISIBLE
                            Log.d(TAG, "onViewCreated: ${it.message}")
                        }
                        ApiResult.Loading -> {
                            loadingDialog().show()
                        }
                        is ApiResult.Success<*> -> {
                            loadingDialog().cancel()
                            binding.wrongOtpErrorMessageTv.visibility=View.GONE
                            login(phone,country,"123")
                            Log.d(TAG, "onViewCreated: success")
                        }
                    }
                }
            }
        }
    }

    private fun getArgs() {
        val args = arguments?.let {
            OtpFragmentArgs.fromBundle(
                it
            )
        }
        // assert args value to vars
        phone = args?.phoneNumber!!
        country = args.country
        viewModel.sendCode(phone, requireActivity())
    }

    private fun login(phone:String,country:String,token:String){
        viewModel.login(phone,country,"123")
        lifecycleScope.launch {
            viewModel.apiState.collect{ apiResult ->
                when(apiResult){
                    ApiResult.Loading -> loadingDialog().show()
                    is ApiResult.Failure -> {
                        loadingDialog().cancel()
                        showToast(requireContext(),R.string.unknownError.toString())
                    }
                    is ApiResult.Success<*> -> {
                        loadingDialog().cancel()
                        val intent=Intent(activity, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    ApiResult.Empty -> {}
                }
            }
        }
    }

}

