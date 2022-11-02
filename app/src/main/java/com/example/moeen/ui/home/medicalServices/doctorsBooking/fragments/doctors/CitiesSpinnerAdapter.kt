package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments.doctors

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.moeen.R
import com.example.moeen.databinding.DoctorsSpinnerItemLayoutBinding
import com.example.moeen.network.model.citiesResponse.CitiesResponse.Data

class CitiesSpinnerAdapter(private val citiesList: MutableList<Data>) : BaseAdapter() {

    override fun getCount(): Int = citiesList.size

    override fun getItem(p0: Int): Any = citiesList[p0]

    override fun getItemId(p0: Int): Long = 0

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = LayoutInflater.from(p2?.context).inflate(R.layout.doctors_spinner_item_layout, null, false)
        val binding = DoctorsSpinnerItemLayoutBinding.bind(view)
        val item = citiesList[p0]

        binding.doctorSpinnerItemId.text = item.name

        if(p0 == 0){
            binding.doctorSpinnerItemId.setTextColor(p2?.context!!.getColor(R.color._92))
        }else{
            binding.doctorSpinnerItemId.setTextColor(p2?.context!!.getColor(R.color.black))
        }

        return binding.root
    }

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }

    fun getCityId(position: Int) : Int = citiesList[position].id
}