package com.example.moeen.ui.onBoarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.moeen.R

class ViewPagerAdapter(fragmentActivity : FragmentActivity)
    : FragmentStateAdapter(fragmentActivity){

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> OnBoardingFragment.newInstance(R.drawable.on_boarding1, R.string.on_boarding1)
            1 -> OnBoardingFragment.newInstance(R.drawable.on_boarding2, R.string.on_boarding2)
            2 -> OnBoardingFragment.newInstance(R.drawable.on_boarding3, R.string.on_boarding3)
            else -> OnBoardingFragment.newInstance(R.drawable.on_boarding1, R.string.on_boarding1)
        }
    }

}