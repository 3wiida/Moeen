package com.example.moeen.ui.home.transportServices.mapsUtility

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor():ViewModel() {
    private lateinit var googleMap:GoogleMap

    fun getMap(map:GoogleMap){
        googleMap=map
    }

}