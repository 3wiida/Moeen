package com.example.moeen.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moeen.R
import com.example.moeen.base.BaseActivity
import com.example.moeen.databinding.ActivityHomeBinding
import com.example.moeen.network.model.homeResponse.HomeResponse
import com.example.moeen.network.model.postsResponse.PostsResponse
import com.example.moeen.utils.resultWrapper.ApiResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel : HomeViewModel by viewModels()
    private val adapter1 = HomeAdapter1()
    private val adapter2 = HomeAdapter1()
    private val adapter3 = HomeAdapter1()
    private val adapter4 = HomeAdapter2()
    private val adapter5 = HomeViewPagerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getHome()
        viewModel.getPosts()

        lifecycleScope.launch(Dispatchers.IO){
            viewModel.homeState.collect{ state ->
                when(state){
                    ApiResult.Empty -> {}
                    ApiResult.Loading -> withContext(Dispatchers.Main){
                        loadingDialog().show()
                    }
                    is ApiResult.Failure -> withContext(Dispatchers.Main){
                        loadingDialog().cancel()
                        showToast(this@HomeActivity, R.string.unknowError.toString())
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
                        showToast(this@HomeActivity, R.string.unknowError.toString())
                    }
                    is ApiResult.Success<*> -> withContext(Dispatchers.Main){
                        loadingDialog().cancel()
                        val result = state.data as PostsResponse
                        preparePostsRecycler(result)
                    }
                }
            }
        }
    }

    private fun prepareRecyclers(result : HomeResponse){
        binding.rvMoveServices.adapter = adapter1
        binding.rvMoveServices.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter1.homeList1 = result.data[0].services

        binding.rvDeadServices.adapter = adapter2
        binding.rvDeadServices.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter2.homeList1 = result.data[1].services

        binding.rvMedicalServices.adapter = adapter3
        binding.rvMedicalServices.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter3.homeList1 = result.data[2].services

        binding.vpHome.adapter = adapter5
        adapter5.list = result.sliders
    }

    private fun preparePostsRecycler(result: PostsResponse){
        binding.rvHowAreYouToday.adapter = adapter4
        binding.rvHowAreYouToday.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter4.homeList2 = result.data
    }
}

/*binding.bottomNavigation.background = null

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

        binding.ivMedicalProfileHome.setOnClickListener {
            startActivity(Intent(this,PathologyFile::class.java))
        }

        binding.tvMedicalProfileHome.setOnClickListener {
            startActivity(Intent(this,PathologyFile::class.java))
        }

    }
}
        binding.rvHowAreYouToday.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)*/