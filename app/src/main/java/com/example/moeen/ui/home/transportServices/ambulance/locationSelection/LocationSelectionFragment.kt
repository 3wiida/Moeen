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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.moeen.R
import com.example.moeen.base.BaseFragment
import com.example.moeen.databinding.FragmentLocationSelectionBinding
import com.example.moeen.network.model.carsTypesResponse.CarsTypesResponse
import com.example.moeen.ui.home.transportServices.ambulance.locationSelection.adapters.CarTypesSpinnerAdapter
import com.example.moeen.utils.resultWrapper.ApiResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class LocationSelectionFragment : BaseFragment(), EasyPermissions.PermissionCallbacks {

    lateinit var binding: FragmentLocationSelectionBinding
    private val viewModel: LocationSelectionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.movingPlaceEt.setOnClickListener {
            if (checkLocationPermission(requireContext())) {
                if (isGPSEnabled(requireActivity())) {
                    view.findNavController()
                        .navigate(R.id.action_locationSelectionFragment_to_mapsFragment)
                } else {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }

            } else {
                requestLocationPermission()
            }
        }

        binding.arrivalPlaceEt.setOnClickListener {
            if (checkLocationPermission(requireContext())) {
                if (isGPSEnabled(requireActivity())) {
                    view.findNavController()
                        .navigate(R.id.action_locationSelectionFragment_to_mapsFragment)
                } else {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            } else {
                requestLocationPermission()
            }
        }
    }


    /** collect flow data */
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
                        binding.ambulanceCarTypeSpinner.adapter =
                            CarTypesSpinnerAdapter(requireContext(), cars.data.toMutableList())
                        loadingDialog().cancel()
                    }
                }
            }
        }
    }


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