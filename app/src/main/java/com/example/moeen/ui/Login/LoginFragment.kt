package com.example.moeen.ui.Login

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.moeen.R
import com.example.moeen.base.BaseFragment
import com.example.moeen.common.Constants.TAG
import com.example.moeen.databinding.FragmentLoginBinding
import com.example.moeen.network.model.countriesResponse.CountriesResponse
import com.example.moeen.ui.home.HomeActivity
import com.example.moeen.utils.removePhoneFirstZero
import com.example.moeen.utils.resultWrapper.ApiResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    @Inject
    lateinit var bundle: Bundle
    lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get countries to put it in ccp
        viewModel.getCountries()
        lifecycleScope.launch {
            viewModel.apiState.collect {
                when (it) {
                    is ApiResult.Failure -> {
                        loadingDialog().cancel()
                        showToast(requireContext(), R.string.unknowError.toString())
                    }
                    ApiResult.Loading -> loadingDialog().show()
                    is ApiResult.Success<*> -> {
                        loadingDialog().cancel()
                        val result = it.data as CountriesResponse
                        var countries = ""
                        for (i in 0 until result.data.size) {
                            countries += result.data[i].code
                            countries += ','
                        }
                        binding.countryCodePicker.setCustomMasterCountries(countries)
                    }
                    ApiResult.Empty -> {}
                }
            }
        }
        //-------------------------------------------------------------------------------

        binding.loginNextBtn.setOnClickListener {
            if (!viewModel.checkPhoneValidation(binding.loginPhoneEt)) {
                binding.loginPhoneContainer.setBackgroundResource(R.drawable._15_red_rect)
                binding.invalidPhoneTv.visibility = View.VISIBLE
            } else {

                binding.loginPhoneContainer.setBackgroundResource(R.drawable._15_gray_rect)
                binding.invalidPhoneTv.visibility = View.INVISIBLE

                val phoneNumber = removePhoneFirstZero(binding.countryCodePicker.selectedCountryCode,binding.loginPhoneEt.text.toString())
                Log.d(TAG, "onViewCreated: $phoneNumber")
                val country = binding.countryCodePicker.selectedCountryNameCode
                view.findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToOtpFragment(
                        phoneNumber,
                        country
                    )
                )
            }
        }

        binding.skipLogin.setOnClickListener{
            start_activity(HomeActivity())
        }
    }


}