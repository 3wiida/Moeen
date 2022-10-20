package com.example.moeen.ui.onBoarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.moeen.common.Constants.TAG
import com.example.moeen.databinding.ActivityOnBoardingBinding
import com.example.moeen.ui.Login.LoginActivity
import com.example.moeen.ui.home.HomeActivity

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val adapter = ViewPagerAdapter(this)
        binding.pager.adapter = adapter
        binding.indicator.setViewPager(binding.pager)

        binding.btnOnBoarding.setOnClickListener {
            if(binding.pager.currentItem != 2){
                binding.pager.currentItem = binding.pager.currentItem.plus(1)
            } else {
                Intent(this, LoginActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }
        }
    }
}