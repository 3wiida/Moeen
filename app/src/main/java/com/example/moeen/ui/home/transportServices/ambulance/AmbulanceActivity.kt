package com.example.moeen.ui.home.transportServices.ambulance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moeen.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AmbulanceActivity : AppCompatActivity() {
    @Inject lateinit var bundle:Bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        bundle.putSerializable("arrivalLocation",null)
        bundle.putSerializable("movingLocation",null)
        finish()
    }
}