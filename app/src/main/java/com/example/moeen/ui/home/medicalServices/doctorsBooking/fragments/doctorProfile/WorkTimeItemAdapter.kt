package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments.doctorProfile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moeen.R
import com.example.moeen.network.model.doctorsResponse.DoctorsResponse.Data.WorkTime.Shift

class WorkTimeItemAdapter : ListAdapter<Shift, WorkTimeItemAdapter.MyViewHolder>(WorkTimeDiff()) {

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val start : TextView = itemView.findViewById(R.id.tvWorkTimeFrom)
        val end : TextView = itemView.findViewById(R.id.tvWorkTimeTo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.work_time_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = currentList[position]

        holder.apply {
            start.text = item.start!!.slice(0..item.start.length-4)
            end.text = item.end!!.slice(0..item.end.length-4)
        }
    }
}

class WorkTimeDiff : DiffUtil.ItemCallback<Shift>(){
    override fun areItemsTheSame(oldItem: Shift, newItem: Shift): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: Shift, newItem: Shift): Boolean {
        return newItem == oldItem
    }
}