package com.example.moeen.ui.home.medicalServices.doctorsBooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moeen.R
import com.example.moeen.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorsBookingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctors_booking)
    }
}