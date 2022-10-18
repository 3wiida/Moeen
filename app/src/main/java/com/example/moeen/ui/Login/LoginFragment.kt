package com.example.moeen.ui.Login

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.moeen.R
import com.example.moeen.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    @Inject lateinit var bundle:Bundle
    lateinit var binding:FragmentLoginBinding
    private val viewModel:LoginViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginNextBtn.setOnClickListener{
            if(!viewModel.checkPhoneValidation(binding.loginPhoneEt)){
                binding.loginPhoneContainer.setBackgroundResource(R.drawable._15_red_rect)
                binding.invalidPhoneTv.visibility=View.VISIBLE
            }else{
                binding.loginPhoneContainer.setBackgroundResource(R.drawable._15_gray_rect)
                binding.invalidPhoneTv.visibility=View.INVISIBLE
                viewModel.sendOtpCode(activity as Activity,"+2${binding.loginPhoneEt.text.toString().trim()}")
                bundle.putString("phoneNumber","+2${binding.loginPhoneEt.text.toString().trim()}")
                view.findNavController().navigate(R.id.action_loginFragment_to_otpFragment)
            }
        }
    }


}