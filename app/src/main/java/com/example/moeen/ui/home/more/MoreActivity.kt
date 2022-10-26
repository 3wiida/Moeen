package com.example.moeen.ui.home.more

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moeen.R
import com.example.moeen.base.BaseActivity
import com.example.moeen.databinding.ActivityMoreBinding
import com.example.moeen.ui.home.RecyclerItem

class MoreActivity : BaseActivity() {

    private lateinit var binding : ActivityMoreBinding
    private val adapter = MoreAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list = listOf(
            RecyclerItem("طرق الدفع المتاحة", R.drawable.ic_credit_card),
            RecyclerItem("انضم كمقدم خدمة", R.drawable.ic_add_person),
            RecyclerItem("انضم إلينا", R.drawable.ic_join_us),
            RecyclerItem("الوظائف", R.drawable.ic_circle),
            RecyclerItem("رسائل", R.drawable.ic_message),
            RecyclerItem("شارك مع صديق", R.drawable.ic_circle),
            RecyclerItem("مركز المساعدة", R.drawable.ic_menu_help_center),
            RecyclerItem("تسجيل خروج", R.drawable.ic_menu_logout)
        )
        prepareRecycler(list)

        binding.btnWalletCharge.setOnClickListener {
            showToast(this, "Sorry, there is no payment right now!!")
        }

    }

    private fun prepareRecycler(list : List<RecyclerItem>){
        adapter.moreList = list
        binding.rvMore.adapter = adapter
        binding.rvMore.layoutManager = LinearLayoutManager(this)
        binding.rvMore.setHasFixedSize(true)
    }
}