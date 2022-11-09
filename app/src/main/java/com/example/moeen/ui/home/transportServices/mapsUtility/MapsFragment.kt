package com.example.moeen.ui.home.transportServices.mapsUtility

import android.annotation.SuppressLint
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.example.moeen.R
import com.example.moeen.base.BaseFragment
import com.example.moeen.network.model.orderRegionResponse.OrderRegionResponse
import com.example.moeen.ui.home.transportServices.ambulance.AmbulanceActivity
import com.example.moeen.ui.home.transportServices.locationSelection.pojo.LocationAddress
import com.example.moeen.utils.resultWrapper.ApiResult
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MapsFragment  : BaseFragment() {

    /** vars */
    private val viewModel:MapsViewModel by viewModels()
    private lateinit var regionResponse: OrderRegionResponse
    private lateinit var pickBtn:Button
    private var markerExist=false
    var lastClickedLocation:LatLng?=null
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
            if(lastClickedLocation==null){
                showToast(requireContext(),"من فضلك فم بتحديد مكان")
            }else{
                viewModel.checkRegion(lastClickedLocation!!.latitude.toString(),lastClickedLocation!!.longitude.toString())
                viewModel.getLocationDetails(requireContext(),lastClickedLocation!!)
            }
        }

        googleMap.setOnMapClickListener { point->

            if(!markerExist){
                googleMap.addMarker(MarkerOptions().position(point).icon(viewModel.bitmapFromVector(requireContext(),R.drawable.ic_location_marker)))
                lastClickedLocation=point
                markerExist=true
            }else{
                googleMap.clear()
                googleMap.addMarker(MarkerOptions().position(point).icon(viewModel.bitmapFromVector(requireContext(),R.drawable.ic_location_marker)))
                lastClickedLocation=point
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val act=activity as AmbulanceActivity
        act.setWhereAmI(1)
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        pickBtn= view.findViewById(R.id.pickLocationBtn)
        mapFragment?.getMapAsync(callback)
        collectRegionResponse()
    }

    private fun collectRegionResponse(){
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.checkRegionResponse.collect{  apiState->
                    when(apiState){
                        ApiResult.Empty -> {}
                        is ApiResult.Failure -> showToast(requireContext(),apiState.message!!)
                        ApiResult.Loading -> {}
                        is ApiResult.Success<*> -> {
                            regionResponse=apiState.data as OrderRegionResponse
                            if(regionResponse.status==0){
                                showToast(requireContext(),"المنطقه غير مغطاه")
                            }else{
                                collectLocationState()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun collectLocationState(){
        val fromWhereInfo= arguments?.let { MapsFragmentArgs.fromBundle(it).fromWhat }
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
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
                                val movingLocation= LocationAddress(
                                    address.getAddressLine(0),
                                    address.latitude.toString(),
                                    address.longitude.toString()
                                )
                                bundle.putSerializable("movingLocation",movingLocation)
                                bundle.putInt("movingGovId",regionResponse.governorate_id)
                                bundle.putInt("movingCityId",regionResponse.city_id)
                                bundle.putInt("movingRegionId",regionResponse.region_id)

                                requireView().findNavController().navigate(R.id.action_mapsFragment_to_locationSelectionFragment)
                            }else{
                                val arrivalLocation= LocationAddress(
                                    address.getAddressLine(0),
                                    address.latitude.toString(),
                                    address.longitude.toString()
                                )
                                bundle.putSerializable("arrivalLocation",arrivalLocation)
                                bundle.putInt("arrivalGovId",regionResponse.governorate_id)
                                bundle.putInt("arrivalCityId",regionResponse.city_id)
                                bundle.putInt("arrivalRegionId",regionResponse.region_id)
                                requireView().findNavController().navigate(R.id.action_mapsFragment_to_locationSelectionFragment)
                            }
                        }
                    }
                }
            }
        }
    }
}