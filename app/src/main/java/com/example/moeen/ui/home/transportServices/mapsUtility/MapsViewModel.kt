package com.example.moeen.ui.home.transportServices.mapsUtility

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moeen.common.Constants.TAG
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {

    private var _deviceLocation = MutableLiveData<Location?>()
    var deviceLocation: LiveData<Location?> = _deviceLocation

    private val PERMISSION_ID: Int = 1
    private lateinit var googleMap: GoogleMap
    private val mFusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun getMap(map: GoogleMap) {
        googleMap = map
    }

    @SuppressLint("MissingPermission")
    fun getDeviceLocation(context: Context) {
        mFusedLocationClient.lastLocation.addOnCompleteListener {
            val location: Location? = it.result
            if (location == null) {
                requestNewLocationData()
            } else {
                _deviceLocation.postValue(location)
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest.build(),
            mLocationCallback,
            Looper.myLooper()
        )
    }


    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(location: LocationResult) {
            _deviceLocation.postValue(location.lastLocation)
            Log.d(TAG, location.lastLocation!!.latitude.toString())
            mFusedLocationClient.removeLocationUpdates(this)
        }
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager: LocationManager =
            (context as Activity).getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun moveCamera(map: GoogleMap, lat: Double, lon: Double) {
        map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, lon)))
    }

    fun addMarker(map: GoogleMap, lat: Double, lon: Double) {
        map.addMarker(MarkerOptions().position(LatLng(lat, lon)))
    }

    fun animateCamera(map: GoogleMap, lat: Double, lon: Double, zoomLevel: Float) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), zoomLevel))
    }
}