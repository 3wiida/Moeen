package com.example.moeen.ui.onBoarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.moeen.R
import me.relex.circleindicator.CircleIndicator3

class OnBoardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        val list = arrayListOf(Fragment1(), Fragment2(), Fragment3())
        val adapter = ViewPagerAdapter(list, supportFragmentManager, lifecycle)
        val vp = findViewById<ViewPager2>(R.id.pager)
        val indicator = findViewById<CircleIndicator3>(R.id.indicator)
        vp.adapter = adapter
        indicator.setViewPager(vp)


    }

    class ViewPagerAdapter(list : ArrayList<Fragment>, manager : FragmentManager,
                           lifecycle: Lifecycle
    ) : FragmentStateAdapter(manager, lifecycle){

        private val fragmentList = list
        override fun getItemCount(): Int {
            return fragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }

    }
}