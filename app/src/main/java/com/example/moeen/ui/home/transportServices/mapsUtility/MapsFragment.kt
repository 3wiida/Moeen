package com.example.moeen.ui.home.transportServices.mapsUtility

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import com.example.moeen.R
import com.example.moeen.common.Constants.TAG

import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class MapsFragment : Fragment() {

    private val viewModel:MapsViewModel by viewModels()
    private lateinit var pickBtn:Button

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        viewModel.getDeviceLocation(requireActivity())
        viewModel.deviceLocation.observe(viewLifecycleOwner){ location->
            googleMap.isMyLocationEnabled=true
            viewModel.animateCamera(googleMap,location!!.latitude,location.longitude,16.0f)
        }
        pickBtn.setOnClickListener{
            val ll=viewModel.pickCentralizedLocation(googleMap)
            val address=viewModel.getLocationDetails(requireContext(),ll)
            Log.d(TAG, address.getAddressLine(0))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        pickBtn= view.findViewById<Button>(R.id.pickLocationBtn)

        mapFragment?.getMapAsync(callback)
    }

}