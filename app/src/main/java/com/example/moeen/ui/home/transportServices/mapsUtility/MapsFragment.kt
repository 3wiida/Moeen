package com.example.moeen.ui.home.transportServices.mapsUtility

import android.annotation.SuppressLint
import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.moeen.R
import com.example.moeen.base.BaseFragment
import com.example.moeen.ui.home.transportServices.ambulance.locationSelection.pojo.LocationAddress
import com.example.moeen.utils.resultWrapper.ApiResult
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MapsFragment  : BaseFragment() {

    /** vars */
    private val viewModel:MapsViewModel by viewModels()
    private lateinit var pickBtn:Button
    @Inject lateinit var bundle:Bundle
    /** ----------------------------------------------------------------------------------------- */


    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

        viewModel.getDeviceLocation(requireActivity())
        viewModel.deviceLocation.observe(viewLifecycleOwner){ location->
            googleMap.isMyLocationEnabled=true
            viewModel.animateCamera(googleMap,location!!.latitude,location.longitude,16.0f)
        }

        /** when user click to pick location */
        pickBtn.setOnClickListener{
            val ll=viewModel.pickCentralizedLocation(googleMap)
            val fromWhereInfo= arguments?.let { MapsFragmentArgs.fromBundle(it).fromWhat }
            viewModel.getLocationDetails(requireContext(),ll)
            lifecycleScope.launch{
                viewModel.locationState.collect{
                    when(it){
                        ApiResult.Empty -> {}
                        is ApiResult.Failure -> {
                            loadingDialog().cancel()
                            showToast(requireContext(),it.message!!)
                        }
                        is ApiResult.Loading -> loadingDialog().show()
                        is ApiResult.Success<*> -> {
                            loadingDialog().cancel()
                            val address =it.data as Address

                            //check if this fragment opened from which Edit Text
                            //if 1 -> moving Edit Text
                            //if 2 -> arrival Edit Text
                            if(fromWhereInfo==1){
                                val movingLocation=LocationAddress(
                                    address.getAddressLine(0),
                                    address.latitude.toString(),
                                    address.longitude.toString()
                                )
                                bundle.putSerializable("movingLocation",movingLocation)
                                requireView().findNavController().navigate(R.id.action_mapsFragment_to_locationSelectionFragment)
                            }else{
                                val arrivalLocation=LocationAddress(
                                    address.getAddressLine(0),
                                    address.latitude.toString(),
                                    address.longitude.toString()
                                )
                                bundle.putSerializable("arrivalLocation",arrivalLocation)
                                requireView().findNavController().navigate(R.id.action_mapsFragment_to_locationSelectionFragment)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        pickBtn= view.findViewById(R.id.pickLocationBtn)
        mapFragment?.getMapAsync(callback)
    }



}