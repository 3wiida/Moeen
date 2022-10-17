package com.example.moeen.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.example.moeen.R
import com.example.moeen.ui.Login.ui.login.LoginActivity
import com.example.moeen.ui.onBoarding.OnBoardingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Splash : AppCompatActivity() {
    private val viewModel:SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_layout)
        Handler(Looper.getMainLooper()).postDelayed({
            if(viewModel.isLoggedIn()){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this,OnBoardingActivity::class.java))
                finish()
            }
        },5000L)
    }


}