package com.example.moeen.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moeen.R
import com.example.moeen.base.BaseActivity
import com.example.moeen.databinding.ActivityHomeBinding
import com.example.moeen.network.model.homeResponse.HomeResponse
import com.example.moeen.network.model.postsResponse.PostsResponse
import com.example.moeen.network.model.profileResponse.ProfileResponse
import com.example.moeen.ui.home.homeAdapters.DrawerAdapter
import com.example.moeen.ui.home.homeAdapters.HomeAdapter1
import com.example.moeen.ui.home.homeAdapters.HomeAdapter2
import com.example.moeen.ui.home.homeAdapters.HomeViewPagerAdapter
import com.example.moeen.ui.home.medicalServices.doctorsBooking.DoctorsBookingActivity
import com.example.moeen.ui.home.more.MoreActivity
import com.example.moeen.ui.home.transportServices.ambulance.AmbulanceActivity
import com.example.moeen.ui.pathologyFile.PathologyFileActivity
import com.example.moeen.utils.PrefUtils.PrefKeys.USER_TOKEN
import com.example.moeen.utils.PrefUtils.PrefUtils.Companion.getFromPref
import com.example.moeen.utils.resultWrapper.ApiResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel : HomeViewModel by viewModels()
    private val adapter1 = HomeAdapter1(this)
    private val adapter2 = HomeAdapter1(this)
    private val adapter3 = HomeAdapter1(this)
    private val adapter4 = HomeAdapter2(this)
    private val adapter5 = HomeViewPagerAdapter()
    private val drawerAdapter = DrawerAdapter()
    private var sliderPagesNumber = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getHome()
        viewModel.getPosts()
        viewModel.getProfile()

        lifecycleScope.launch(Dispatchers.IO){
            viewModel.homeState.collect{ state ->
                when(state){
                    ApiResult.Empty -> {}
                    ApiResult.Loading -> withContext(Dispatchers.Main){
                        loadingDialog().show()
                    }
                    is ApiResult.Failure -> withContext(Dispatchers.Main){
                        loadingDialog().cancel()
                        showToast(this@HomeActivity, R.string.unknownError.toString())
                    }
                    is ApiResult.Success<*> -> withContext(Dispatchers.Main){
                        loadingDialog().cancel()
                        val result = state.data as HomeResponse
                        prepareRecyclers(result)
                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO){
            viewModel.postsState.collect{ state ->
                when(state){
                    ApiResult.Empty -> {}
                    ApiResult.Loading -> withContext(Dispatchers.Main){
                        loadingDialog().show()
                    }
                    is ApiResult.Failure -> withContext(Dispatchers.Main){
                        loadingDialog().cancel()
                        showToast(this@HomeActivity, R.string.unknownError.toString())
                    }
                    is ApiResult.Success<*> -> withContext(Dispatchers.Main){
                        loadingDialog().cancel()
                        val result = state.data as PostsResponse
                        preparePostsRecycler(result)
                    }
                    ApiResult.Empty -> {}
                }
            }
        }

        val token = getFromPref(this, USER_TOKEN, "")
        if(token != ""){
            lifecycleScope.launch(Dispatchers.IO){
                viewModel.profileState.collect{ state ->
                    when(state){
                        ApiResult.Empty -> {}
                        ApiResult.Loading -> withContext(Dispatchers.Main){
                            loadingDialog().show()
                        }
                        is ApiResult.Failure -> withContext(Dispatchers.Main){
                            loadingDialog().cancel()
                            showToast(this@HomeActivity, R.string.unknownError.toString())
                        }
                        is ApiResult.Success<*> -> withContext(Dispatchers.Main){
                            loadingDialog().cancel()
                            val result = (state.data as ProfileResponse).data.user
                            Glide.with(this@HomeActivity).load(result.photo).into(binding.ivHeaderProfileImage)
                            binding.tvHeaderName.text = result.name
                            val phone=result.phone.slice(3 until result.phone.length)
                            binding.tvHeaderPhone.text = phone
                        }
                    }
                }
            }
        }

        var currentPage = 0
        val handler = Handler()
        val update = Runnable {
            if (currentPage == sliderPagesNumber) {
                currentPage = 0
            }
            binding.appBarHome.vpHome.setCurrentItem(currentPage++, true)
        }

        val timer = Timer()

        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 100, 3000)


        adapter1.onItemClick = { item->
            when(item.id){
                1 -> startActivity(Intent(this,AmbulanceActivity::class.java))
            }
        }

        adapter3.onItemClick = { item ->
            when(item.id){
                7 -> {
                    startActivity(Intent(this, DoctorsBookingActivity::class.java))
                }
                8 -> {
                    // TODO: المختبر والاشعة
                }
                9 -> {
                    // TODO: الصيدليات
                }
                11 -> {
                    // TODO: الرعايا الطبية
                }
            }
        }

    }

    private fun prepareRecyclers(result : HomeResponse){
        binding.appBarHome.rvMoveServices.adapter = adapter1
        binding.appBarHome.rvMoveServices.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter1.homeList1 = result.data[0].services

        binding.appBarHome.tvMoveServices.visibility = View.VISIBLE
        binding.appBarHome.rvMoveServices.visibility = View.VISIBLE

        binding.appBarHome.rvDeadServices.adapter = adapter2
        binding.appBarHome.rvDeadServices.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter2.homeList1 = result.data[1].services

        binding.appBarHome.tvDeadServices.visibility = View.VISIBLE
        binding.appBarHome.rvDeadServices.visibility = View.VISIBLE

        binding.appBarHome.rvMedicalServices.adapter = adapter3
        binding.appBarHome.rvMedicalServices.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter3.homeList1 = result.data[2].services

        binding.appBarHome.tvMedicalServices.visibility = View.VISIBLE
        binding.appBarHome.rvMedicalServices.visibility = View.VISIBLE

        binding.appBarHome.vpHome.adapter = adapter5
        adapter5.list = result.sliders
        sliderPagesNumber = result.sliders.size

        binding.appBarHome.vpHome.visibility = View.VISIBLE


        binding.rvMenu.adapter = drawerAdapter
        binding.rvMenu.layoutManager = LinearLayoutManager(this)
        binding.rvMenu.setHasFixedSize(true)
        drawerAdapter.drawerList = listOf(
            RecyclerItem("الحجوزات والطلبات", R.drawable.ic_menu_reservations),
            RecyclerItem("الدعم والمساعدة", R.drawable.ic_menu_support),
            RecyclerItem("الإشعارات", R.drawable.ic_menu_reports),
            RecyclerItem("مركز المساعدة", R.drawable.ic_menu_help_center),
            RecyclerItem("شارك مع صديق", R.drawable.ic_circle),
            RecyclerItem("تسجيل الخروج", R.drawable.ic_menu_logout)
        )

        // TODO: Launch every activity
        drawerAdapter.onItemClicked = {
            showToast(this, it.title)
        }

        binding.appBarHome.ivDrawerLayout.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.navView, true)
        }

        binding.appBarHome.moreHomeLayout.setOnClickListener {
            // TODO: Menu & More Changes
//            binding.drawerLayout.openDrawer(binding.navView, true)
            startActivity(Intent(this, MoreActivity::class.java))
        }

        binding.appBarHome.medicalProfileLayout.setOnClickListener {
            Intent(this, PathologyFileActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun preparePostsRecycler(result: PostsResponse){
        binding.appBarHome.rvHowAreYouToday.adapter = adapter4
        binding.appBarHome.rvHowAreYouToday.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter4.homeList2 = result.data

        binding.appBarHome.tvHowAreYouToday.visibility = View.VISIBLE
        binding.appBarHome.rvHowAreYouToday.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(binding.navView)){
            binding.drawerLayout.closeDrawer(binding.navView, true)
        }else{
            super.onBackPressed()
        }
    }
}
