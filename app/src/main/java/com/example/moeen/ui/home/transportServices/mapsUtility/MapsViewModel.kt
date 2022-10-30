package com.example.moeen.ui.home.transportServices.mapsUtility

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.util.Log
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
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {

    private var _deviceLocation = MutableLiveData<Location?>()
    var deviceLocation: LiveData<Location?> = _deviceLocation

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
        val mLocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest.build(),
            mLocationCallback,
            Looper.myLooper()
        )
    }


    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(location: LocationResult) {
            _deviceLocation.postValue(location.lastLocation)
            mFusedLocationClient.removeLocationUpdates(this)
        }
    }


    fun pickCentralizedLocation(map: GoogleMap):LatLng {
        val lat=map.cameraPosition.target.latitude
        val lon=map.cameraPosition.target.longitude
        return LatLng(lat,lon)
    }

    fun getLocationDetails(context: Context, latLong: LatLng): Address {
        val arabicLocale=Locale("ar")
        val geocoder = Geocoder(context, arabicLocale)
        return geocoder.getFromLocation(latLong.latitude, latLong.longitude, 1)[0]
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