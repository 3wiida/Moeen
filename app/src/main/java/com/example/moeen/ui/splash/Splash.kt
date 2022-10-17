package com.example.moeen.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.moeen.R
import com.example.moeen.ui.onBoarding.OnBoardingActivity

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,OnBoardingActivity::class.java))
            finish()
        },1000L)
        //COMMENT
        //secondcomment
    }
}