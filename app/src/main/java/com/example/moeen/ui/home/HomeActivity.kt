package com.example.moeen.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moeen.R
import com.example.moeen.databinding.ActivityHomeBinding
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter1: HomeAdapter1
    private lateinit var adapter2: HomeAdapter1
    private lateinit var adapter3: HomeAdapter1
    private lateinit var adapter4: HomeAdapter2
    private lateinit var adapter5: HomeViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.bottomNavigation.background = null

        adapter1 = HomeAdapter1()
        adapter2 = HomeAdapter1()
        adapter3 = HomeAdapter1()
        adapter4 = HomeAdapter2()
        adapter5 = HomeViewPagerAdapter()

        val list1 = listOf(
            HomeItem1(R.drawable.ambulance, "نقل المرضي"),
            HomeItem1(R.drawable.ambulance, "نقل المرضي"),
            HomeItem1(R.drawable.ambulance, "نقل المرضي"),
            HomeItem1(R.drawable.ambulance, "نقل المرضي"),
            HomeItem1(R.drawable.ambulance, "نقل المرضي")
        )

        val list2 = listOf(
            HomeItem1(R.drawable.how_are_you, "معدل السكر الطبيعي"),
            HomeItem1(R.drawable.how_are_you, "معدل السكر الطبيعي"),
            HomeItem1(R.drawable.how_are_you, "معدل السكر الطبيعي"),
            HomeItem1(R.drawable.how_are_you, "معدل السكر الطبيعي"),
            HomeItem1(R.drawable.how_are_you, "معدل السكر الطبيعي")
        )

        val list3 = listOf(
            PagerItem(R.drawable.home_pager_image1),
            PagerItem(R.drawable.home_pager_image1),
            PagerItem(R.drawable.home_pager_image1),
            PagerItem(R.drawable.home_pager_image1),
            PagerItem(R.drawable.home_pager_image1)
        )

        adapter1.homeList1 = list1
        adapter2.homeList1 = list1
        adapter3.homeList1 = list1
        adapter4.homeList2 = list2
        adapter5.list = list3

        binding.rvMoveServices.adapter = adapter1
        binding.rvDeadServices.adapter = adapter2
        binding.rvMedicalServices.adapter = adapter3
        binding.rvHowAreYouToday.adapter = adapter4
        binding.vpHome.adapter = adapter5

        binding.rvMoveServices.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDeadServices.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvMedicalServices.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHowAreYouToday.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)



    }
}