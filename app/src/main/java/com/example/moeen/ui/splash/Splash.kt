package com.example.moeen.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import com.example.moeen.R
import com.example.moeen.common.Constants.TAG
import com.example.moeen.ui.Login.LoginActivity
import com.example.moeen.ui.home.HomeActivity
import com.example.moeen.ui.onBoarding.OnBoardingActivity
import com.example.moeen.utils.PrefUtils.PrefKeys.IS_FIRST_TIME
import com.example.moeen.utils.PrefUtils.PrefUtils
import com.example.moeen.utils.PrefUtils.PrefUtils.Companion.saveInPref
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Splash : AppCompatActivity() {
    private val viewModel:SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_layout)
        Handler(Looper.getMainLooper()).postDelayed({
            if(viewModel.isFirstTime() as Boolean){
                saveInPref(this,IS_FIRST_TIME,false)
                startActivity(Intent(this,OnBoardingActivity::class.java))
                finish()
            }else{
                if(viewModel.isLoggedIn()){
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                }
            }
        },1000L)

    }


}