package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.moeen.R
import com.example.moeen.base.BaseFragment
import com.example.moeen.databinding.FragmentConfirmationDataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmationDataFragment : BaseFragment() {

    private lateinit var binding: FragmentConfirmationDataBinding
    private val args : ConfirmationDataFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirmation_data, container, false)

        binding.include.tvFragmentTitle.text = "تأكيد البيانات"
        binding.include.ivBackArrow.setOnClickListener {
            view?.findNavController()?.navigateUp()
        }

        binding.etDoctorName.setText(args.doctorName)
        binding.etDoctorSpeciality.setText(args.doctorSpeciality)
        val text = "${args.day}, ${args.startTime}:${args.endTime}"
        binding.etReserveTime.setText(text)


        binding.btnConfirmData.setOnClickListener {
            showToast(requireContext(), "Done")
            Handler(Looper.getMainLooper()).postDelayed({
                activity?.finish()
            }, 1000L)
        }


        return binding.root
    }
}