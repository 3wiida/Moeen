package com.example.moeen.ui.Login

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.ContactsContract.RawContacts.Data
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.moeen.R
import com.example.moeen.common.Constants.TAG
import com.example.moeen.databinding.FragmentOtpBinding
import com.example.moeen.ui.home.HomeActivity
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OtpFragment : Fragment() {
    @Inject lateinit var bundle: Bundle
    lateinit var binding: FragmentOtpBinding
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_otp, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        binding.resendOtpTv.setOnClickListener {
            viewModel.startResendOtpTimer()
            binding.resendOtpTv.isEnabled = false
            binding.resendOtpTv.setTextColor(Color.parseColor("#8F9596"))
            binding.resendOtpTimer.visibility = View.VISIBLE
        }

        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.otpNextBtn.setOnClickListener {
            if(binding.pinView.text!!.isEmpty()){
                binding.wrongOtpErrorMessageTv.visibility = View.VISIBLE
            }else{
                val credential = PhoneAuthProvider.getCredential(
                    bundle.getString("OTPCode")!!,
                    binding.pinView.text.toString()
                )
                viewModel.signInWithCredential(credential)
                viewModel.authValidation.observe(viewLifecycleOwner) {
                    if (it == "Success") {
                        binding.wrongOtpErrorMessageTv.visibility = View.GONE
                        startActivity(Intent(activity, HomeActivity::class.java))
                        activity?.finish()
                    } else {
                        binding.wrongOtpErrorMessageTv.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}

