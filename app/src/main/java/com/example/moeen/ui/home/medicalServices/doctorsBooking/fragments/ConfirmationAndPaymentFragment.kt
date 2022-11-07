package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.moeen.R
import com.example.moeen.base.BaseFragment
import com.example.moeen.databinding.FragmentConfirmationAndPaymentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmationAndPaymentFragment : BaseFragment() {

    private lateinit var binding: FragmentConfirmationAndPaymentBinding
    private val args : ConfirmationAndPaymentFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirmation_and_payment, container, false)

        binding.btnConfirmPayment.setOnClickListener {
            view?.findNavController()?.navigate(ConfirmationAndPaymentFragmentDirections
                .actionConfirmationAndPaymentFragmentToConfirmationDataFragment(args.doctorName, args.doctorSpeciality, args.day, args.startTime, args.endTime))
        }


        return binding.root
    }
}