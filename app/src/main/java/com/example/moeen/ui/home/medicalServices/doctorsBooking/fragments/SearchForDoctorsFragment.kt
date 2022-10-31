package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.moeen.R
import com.example.moeen.base.BaseFragment
import com.example.moeen.databinding.FragmentSearchForDoctorsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchForDoctorsFragment : BaseFragment() {

    private lateinit var binding : FragmentSearchForDoctorsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_for_doctors, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearchBySpecialization.setOnClickListener {
            view.findNavController().navigate(R.id.action_searchForDoctorsFragment_to_doctorsFragment)
        }
        binding.btnSearchByName.setOnClickListener {
            view.findNavController().navigate(R.id.action_searchForDoctorsFragment_to_doctorsFragment)
        }
    }
}