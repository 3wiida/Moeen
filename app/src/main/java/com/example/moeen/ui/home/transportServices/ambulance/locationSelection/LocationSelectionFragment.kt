package com.example.moeen.ui.home.transportServices.ambulance.locationSelection

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.moeen.R
import com.example.moeen.base.BaseFragment
import com.example.moeen.common.Constants.TAG
import com.example.moeen.databinding.FragmentLocationSelectionBinding
import com.example.moeen.network.model.carsTypesResponse.CarsTypesResponse
import com.example.moeen.network.model.carsTypesResponse.Data
import com.example.moeen.network.model.claculatePriceResponse.CalculatePriceResponse
import com.example.moeen.network.model.orderRegionResponse.OrderRegionResponse
import com.example.moeen.ui.home.transportServices.ambulance.locationSelection.adapters.CarTypesSpinnerAdapter
import com.example.moeen.ui.home.transportServices.ambulance.locationSelection.pojo.LocationAddress
import com.example.moeen.utils.resultWrapper.ApiResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@AndroidEntryPoint
class LocationSelectionFragment : BaseFragment(), EasyPermissions.PermissionCallbacks {

    /** vars */
    @Inject lateinit var bundle: Bundle
    private lateinit var binding: FragmentLocationSelectionBinding
    private lateinit var scaleDown:Animation
    private lateinit var scaleUp:Animation
    private val viewModel: LocationSelectionViewModel by viewModels()
    private var movingGovId:Int=0
    private var movingCityId:Int=0
    private var arrivalCityId:Int=0
    private var arrivalGovId:Int=0
    /** -------------------------------------------------------------------------------- */



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scaleDown=AnimationUtils.loadAnimation(requireContext(),R.anim.scale_down)
        scaleUp=AnimationUtils.loadAnimation(requireContext(),R.anim.scale_up)
        viewModel.getCarTypes()
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_location_selection,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        collectData()
        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val arrivalLocation=bundle.getSerializable("arrivalLocation") as LocationAddress?
        val movingLocation=bundle.getSerializable("movingLocation") as LocationAddress?

        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }


        /** Handle click on moving Edit Text */
        binding.movingPlaceEt.setOnClickListener {
            if (checkLocationPermission(requireContext())) {
                if (isGPSEnabled(requireActivity())) {
                    val fromWhereInfo=LocationSelectionFragmentDirections.actionLocationSelectionFragmentToMapsFragment(1)
                    view.findNavController().navigate(fromWhereInfo)
                } else {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }

            } else {
                requestLocationPermission()
            }
        }

        /** Handle click on arrival Edit Text */
        binding.arrivalPlaceEt.setOnClickListener {
            if (checkLocationPermission(requireContext())) {
                if (isGPSEnabled(requireActivity())) {
                    val fromWhereInfo=LocationSelectionFragmentDirections.actionLocationSelectionFragmentToMapsFragment(2)
                    view.findNavController().navigate(fromWhereInfo)
                } else {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            } else {
                requestLocationPermission()
            }
        }
        binding.arrivalPlaceEt.setText(arrivalLocation?.addressLine)
        binding.movingPlaceEt.setText(movingLocation?.addressLine)



        /** Handle calculate price btn */
        binding.calcPriceBtn.setOnClickListener{
            if(binding.calcPriceBtn.text=="تأكيد"){
                Log.d(TAG, "onViewCreated: that's it")
            }else{
                val movingPlace=binding.movingPlaceEt.text.toString()
                val arrivalPlace=binding.arrivalPlaceEt.text.toString()
                val carId=binding.ambulanceCarTypeSpinner.selectedItem as Data
                if(viewModel.isFormValid(movingPlace,arrivalPlace,carId.id.toString())){
                    viewModel.checkOrderRegion(movingLocation!!.lat,movingLocation.lon)
                    collectCheckRegionResponseForMoving()
                    viewModel.checkOrderRegion(arrivalLocation!!.lat,arrivalLocation.lon)
                    collectCheckRegionResponseForArrival()
                    viewModel.calculatePrice(movingGovId,arrivalGovId,movingCityId,arrivalCityId,carId.id)
                    collectCalculatePriceResponse()
                    binding.calcPriceBtn.text="تأكيد"
                    //scaleDown.fillAfter=true
                    binding.calcPriceBtn.visibility=View.GONE
                    binding.confirmationCardView.visibility=View.VISIBLE
                    //binding.confirmationCardView.animate().translationX(100f)
                    initAnimation(binding.confirmationCardView)
                    scaleUp.fillAfter=true
                }
            }
        }

    }



    /** collect all response data */
    private fun collectData() {
        lifecycleScope.launch {
            viewModel.carTypes.collect { result ->
                when (result) {
                    ApiResult.Empty -> {}
                    is ApiResult.Failure -> {
                        loadingDialog().cancel()
                        showToast(requireContext(), result.message!!)
                    }
                    ApiResult.Loading -> {
                        loadingDialog().show()
                    }
                    is ApiResult.Success<*> -> {
                        val cars = result.data as CarsTypesResponse
                        binding.ambulanceCarTypeSpinner.adapter = CarTypesSpinnerAdapter(requireContext(), cars.data.toMutableList())

                        loadingDialog().cancel()
                    }
                }
            }
        }
    }


    private fun collectCheckRegionResponseForMoving(){
        lifecycleScope.launch {
            viewModel.checkRegionResponse.collect{
                when(it){
                    ApiResult.Empty -> {}
                    is ApiResult.Failure -> {
                        loadingDialog().cancel()
                        showToast(requireContext(),"مكان التحرك خارج خدمتنا")
                    }
                    ApiResult.Loading -> loadingDialog().show()
                    is ApiResult.Success<*> -> {
                        val result=it.data as OrderRegionResponse
                        movingGovId=result.governorate_id
                        movingCityId=result.city_id
                        loadingDialog().cancel()
                    }
                }
            }
        }
    }


    private fun collectCheckRegionResponseForArrival(){
        lifecycleScope.launch {
            viewModel.checkRegionResponse.collect{
                when(it){
                    ApiResult.Empty -> {}
                    is ApiResult.Failure -> {
                        loadingDialog().cancel()
                        showToast(requireContext(),"منطقه الوصول غير مغطاه")
                    }
                    ApiResult.Loading -> loadingDialog().show()
                    is ApiResult.Success<*> -> {
                        val result=it.data as OrderRegionResponse
                        arrivalCityId=result.city_id
                        arrivalGovId=result.governorate_id
                        loadingDialog().cancel()
                    }
                }
            }
        }
    }

    private fun collectCalculatePriceResponse(){
        lifecycleScope.launch{
            viewModel.calculatePriceResponse.collect{
                when(it){
                    ApiResult.Empty -> {}
                    is ApiResult.Failure -> showToast(requireContext(),it.message!!)
                    ApiResult.Loading -> {}
                    is ApiResult.Success<*> -> {
                        val result=it.data as CalculatePriceResponse
                        try{
                            showToast(requireContext(),result.data.price.toString())
                        }catch (e:Exception){
                            showToast(requireContext(),"لا يوجد توصيل فى المدينة")
                        }

                    }
                }
            }
        }
    }

    private fun initAnimation(view:View){
        val translate=TranslateAnimation(
            -(view.width.toFloat()),view.width.toFloat()/3,0f,0f
        )
        translate.fillAfter=true
        translate.duration=1000
        view.startAnimation(translate)
    }

    /** -------------------------------------------------------------------------------------------------------*/




    /** this section for handle location permission */
    private fun checkLocationPermission(context: Context) = EasyPermissions.hasPermissions(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )



    private fun requestLocationPermission() {
        if (checkLocationPermission(requireContext())) {
            return
        }

        EasyPermissions.requestPermissions(
            this@LocationSelectionFragment,
            "تطبيق معين يحتاج صلاحيات الموقع حتى تستطيع استحدام هذه الخدمة",
            0,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    }



    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}



    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this@LocationSelectionFragment, perms)) {
            initPermissionDialog()
        } else {
            requestLocationPermission()
        }
    }



    private fun isGPSEnabled(context: Context): Boolean {
        val locationManger = (context as Activity).getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManger.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(0, permissions, grantResults, this)
    }




    private fun initPermissionDialog(){
        val permissionDialog=Dialog(requireContext())
        permissionDialog.setContentView(R.layout.permission_request_dialog)
        permissionDialog.window?.setBackgroundDrawableResource(R.drawable._15_white_rect)
        permissionDialog.window?.findViewById<Button>(R.id.settingBtn)?.setOnClickListener{
            val intent=Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri=Uri.fromParts("package",activity?.packageName,null)
            intent.data = uri
            startActivity(intent)
            permissionDialog.cancel()
        }
        permissionDialog.window?.findViewById<TextView>(R.id.cancelDialogBtn)?.setOnClickListener {
            permissionDialog.cancel()
        }
        permissionDialog.show()
    }
}