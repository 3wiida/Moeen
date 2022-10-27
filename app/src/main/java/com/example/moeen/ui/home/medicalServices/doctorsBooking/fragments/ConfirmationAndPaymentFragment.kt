package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.example.moeen.R

class ConfirmationAndPaymentFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment




        return inflater.inflate(R.layout.fragment_confirmation_and_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btn = view.findViewById<Button>(R.id.btnNextConfirmation)
        btn?.setOnClickListener {
            view.findNavController().navigate(R.id.action_confirmationAndPaymentFragment_to_confirmationDataFragment)
        }
    }
}