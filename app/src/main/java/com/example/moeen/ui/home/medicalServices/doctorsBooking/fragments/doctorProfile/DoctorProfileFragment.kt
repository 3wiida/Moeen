package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments.doctorProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moeen.R
import com.example.moeen.databinding.FragmentDoctorProfileBinding
import com.example.moeen.network.model.doctorsResponse.DoctorsResponse.Data
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class DoctorProfileFragment : Fragment() {

    lateinit var binding: FragmentDoctorProfileBinding
    private val args : DoctorProfileFragmentArgs by navArgs()
    private lateinit var doctor : Data
    private var workTimesAdapter = WorkTimesAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doctor_profile, container, false)
        doctor = args.doctor

        val doctorName = doctor.name!!.split(" ")

        if(doctorName.size >= 2){
            binding.include.tvFragmentTitle.text = "دكتور ${doctorName[0]} ${doctorName[1]}"
        } else {
            binding.include.tvFragmentTitle.text = "دكتور ${doctorName[0]}"
        }

        binding.include.ivBackArrow.setOnClickListener {
            view?.findNavController()?.navigateUp()
        }


        /** Get data from the past fragment */
        Glide.with(requireContext()).load(doctor.photo).into(binding.ivDoctorProfileImage)
        binding.tvDoctorProfileName.text = doctor.name
        binding.doctorProfileRating.rating = doctor.rate!!.toFloat()
        binding.tvDoctorProfileSpeciality.text = doctor.specialtyId!!.name
        binding.tvDoctorProfileLocation.text = doctor.address

        if(doctor.medicalPrice == null){
            binding.tvDoctorProfilePrice.text = "0"
        }else {
            binding.tvDoctorProfilePrice.text = doctor.visitPrice.toString()
        }

        val list : MutableList<Data.WorkTime> = ArrayList()
        doctor.workTimes!!.forEach {
            if(it!!.shifts!!.isNotEmpty()){
                list.add(it)
            }
        }
        workTimesAdapter.submitList(list)
        binding.rvDoctorWorkTimes.adapter = workTimesAdapter
        binding.rvDoctorWorkTimes.layoutManager = LinearLayoutManager(requireContext())


        /** Handle reserve click */
        workTimesAdapter.onItemClicked = {
            view?.findNavController()?.navigate(DoctorProfileFragmentDirections
            .actionDoctorProfileFragmentToConfirmationAndPaymentFragment(doctor.name!!, doctor.specialtyId!!.name!!, it.day!!, it.start!!, it.end!!, doctor.visitPrice!!))
        }

        return binding.root
    }
}