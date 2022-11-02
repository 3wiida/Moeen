package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.moeen.R
import com.example.moeen.databinding.FragmentDoctorProfileBinding
import com.example.moeen.ui.Login.OtpFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorProfileFragment : Fragment() {

    private var doctorId = 0
    lateinit var binding: FragmentDoctorProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doctor_profile, container, false)
        getArgs()


        binding.tvProfile.text = "Doctor $doctorId Profile"

        return binding.root
    }

    // TODO: Get All Data
    private fun getArgs() {
        val args = arguments?.let {
            DoctorProfileFragmentArgs.fromBundle(
                it
            )
        }

        doctorId = args?.doctorId!!
    }
}