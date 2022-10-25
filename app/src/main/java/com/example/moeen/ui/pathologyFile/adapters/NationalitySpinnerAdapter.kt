package com.example.moeen.ui.pathologyFile.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.moeen.R
import com.example.moeen.databinding.SpinnerItemLayoutBinding
import com.example.moeen.network.model.profileResponse.Country

class NationalitySpinnerAdapter (private val context: Context, private val list:MutableList<Country>): BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view= LayoutInflater.from(context).inflate(R.layout.spinner_item_layout,null,false)
        val binding= SpinnerItemLayoutBinding.bind(view)
        val item=list[p0]
        binding.spinnerItemId.text=item.nationality
        return binding.root
    }

    @SuppressLint("InflateParams")
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view= LayoutInflater.from(context).inflate(R.layout.spinner_item_layout,null,false)
        val binding= SpinnerItemLayoutBinding.bind(view)
        if(position==0){
            binding.spinnerItemId.setTextColor(context.getColor(R.color._3c))
        }else{
            binding.spinnerItemId.setTextColor(context.getColor(R.color.black))
        }
        val item=list[position]
        binding.spinnerItemId.text=item.nationality
        return binding.root
    }
}