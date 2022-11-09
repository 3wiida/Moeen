package com.example.moeen.ui.home.transportServices.mapsUtility

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.example.moeen.utils.resultWrapper.ApiResult
import com.example.moeen.utils.resultWrapper.ResultWrapper
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
@SuppressLint("StaticFieldLeak")
class MapsViewModel @Inject constructor( @ApplicationContext private val context: Context , private val repo:MapsRepository) : ViewModel() {

    private var _deviceLocation = MutableLiveData<Location?>()
    var deviceLocation: LiveData<Location?> = _deviceLocation

    private lateinit var googleMap: GoogleMap
    private val mFusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    private var _locationState:MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Loading)
    var locationState:StateFlow<ApiResult> = _locationState

    private var _checkRegionResponse:MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Empty)
    var checkRegionResponse= _checkRegionResponse.asStateFlow()

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

    fun getLocationDetails(context: Context, latLong: LatLng) {
        _locationState.value = ApiResult.Loading
        val arabicLocale=Locale("ar")
        val geocoder = Geocoder(context, arabicLocale)
        try {
            _locationState.value=ApiResult.Success(geocoder.getFromLocation(latLong.latitude, latLong.longitude, 1)[0])
        }catch (e:Exception){
            _locationState.value=ApiResult.Failure(message = "حاول مجددا فى وقت لاحق")
        }

    }

    fun checkRegion(lat:String,lon:String){
        viewModelScope.launch(Dispatchers.IO){
            when(val response=repo.checkRegion(lat,lon)){
                is ResultWrapper.Failure -> _checkRegionResponse.value=ApiResult.Failure(message = response.message)
                is ResultWrapper.Success -> {
                    _checkRegionResponse.value=ApiResult.Success(data = response.results)
                    delay(300L)
                }
            }
        }
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

    fun bitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        // below line is use to generate a drawable.
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        // below line is use to set bounds to our vector drawable.
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )

        // below line is use to create a bitmap for our
        // drawable which we have added.
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // below line is use to add bitmap in our canvas.
        val canvas = Canvas(bitmap)

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas)

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}