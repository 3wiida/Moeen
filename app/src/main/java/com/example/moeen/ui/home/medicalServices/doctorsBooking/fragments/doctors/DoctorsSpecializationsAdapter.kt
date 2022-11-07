package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments.doctors

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moeen.R
import com.example.moeen.common.Constants.TAG
import com.example.moeen.network.model.specializationsResponse.SpecializationsResponse.Data
import com.example.moeen.utils.dpToPx


class DoctorsSpecializationsAdapter(private val context: Context) : ListAdapter<Data, DoctorsSpecializationsAdapter.MyViewHolder>(DifferCallback()) {

    inner class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.tvDoctorsSpecialization)
    }

    var onItemClicked : ((Data) -> Unit) ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.doctors_specialization_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)

        holder.name.text = item.name

        // TODO: Fix the first item margin
        if(position == 0){
            val value = holder.itemView.marginStart
            val param = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(value, 0, dpToPx(16, context), 0)
            holder.itemView.layoutParams = param
        }

        if(item.checked){
            holder.itemView.setBackgroundResource(R.drawable._30_blue_rect)
            holder.name.setTextColor(context.getColor(R.color.white))
        } else{
            holder.itemView.setBackgroundResource(R.drawable._30_gray_rect)
            holder.name.setTextColor(context.getColor(R.color.black))
        }

        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(item)
        }
    }
}

class DifferCallback : DiffUtil.ItemCallback<Data>(){
    override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
        return oldItem == newItem
    }
}
