package com.example.moeen.ui.home.transportServices.locationSelection.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.moeen.R
import com.example.moeen.databinding.SpinnerItemLayoutBinding
import com.example.moeen.network.model.carsTypesResponse.Data

class CarTypesSpinnerAdapter(private val context: Context,private val list:MutableList<Data>) :BaseAdapter() {
    override fun getCount() = list.size

    override fun getItem(position: Int) = list[position]

    override fun getItemId(p0: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val view=LayoutInflater.from(context).inflate(R.layout.spinner_item_layout,null,false)
        val binding=SpinnerItemLayoutBinding.bind(view)
        val item=list[position]
        binding.spinnerItemId.text=item.name
        return binding.root
    }

}