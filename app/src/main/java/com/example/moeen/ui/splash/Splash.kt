package com.example.moeen.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.moeen.R
import com.example.moeen.ui.onBoarding.OnBoardingActivity
import com.example.moeen.utils.resultWrapper.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_layout)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,OnBoardingActivity::class.java))
            finish()
        },5000L)
    }


}