package com.example.moeen.ui.pathologyFile.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.compose.ui.layout.Layout
import com.example.moeen.R
import com.example.moeen.databinding.SpinnerItemLayoutBinding
import com.example.moeen.network.model.profileResponse.Region

class RegionSpinnerAdapter(private val context:Context,private val list:MutableList<Region>) :BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view= LayoutInflater.from(context).inflate(R.layout.spinner_item_layout,null,false)
        val binding=SpinnerItemLayoutBinding.bind(view)
        val item=list[p0]
        binding.spinnerItemId.text="${item.city?.state?.name} - ${item.city?.name} - ${item.name}"
        return binding.root
    }

}