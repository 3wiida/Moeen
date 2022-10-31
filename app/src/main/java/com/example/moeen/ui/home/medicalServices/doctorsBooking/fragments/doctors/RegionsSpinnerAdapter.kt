package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments.doctors

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.moeen.R
import com.example.moeen.databinding.SpinnerItemLayoutBinding
import com.example.moeen.network.model.regionsResponse.RegionsResponse.Data

class RegionsSpinnerAdapter : BaseAdapter() {

    var regionsList : MutableList<Data> = ArrayList()

    override fun getCount(): Int = regionsList.size

    override fun getItem(p0: Int): Any = regionsList[p0]

    override fun getItemId(p0: Int): Long = 0

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = LayoutInflater.from(p2?.context).inflate(R.layout.spinner_item_layout, null, false)
        val binding = SpinnerItemLayoutBinding.bind(view)
        val item = regionsList[p0]

        binding.spinnerItemId.text = item.name

        if(p0 == 0){
            binding.spinnerItemId.setTextColor(p2?.context!!.getColor(R.color._92))
        }else{
            binding.spinnerItemId.setTextColor(p2?.context!!.getColor(R.color.black))
        }

        return binding.root
    }

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }
}