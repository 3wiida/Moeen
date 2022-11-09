package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments.doctorProfile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.*
import com.example.moeen.R
import com.example.moeen.network.model.doctorsResponse.DoctorsResponse.Data.WorkTime

class WorkTimesAdapter : ListAdapter<WorkTime, WorkTimesAdapter.MyViewHolder>(WorkTimesDiff()) {

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val date : TextView = itemView.findViewById(R.id.tvWorkTimeItemDayDate)
        val day : TextView = itemView.findViewById(R.id.tvWorkTimeItemDay)
        val rvTimes : RecyclerView = itemView.findViewById(R.id.rvDayWorkTimes)
        val btn : TextView = itemView.findViewById(R.id.tvReserveWorkTime)
    }

    var onItemClicked : ((WorkTime.Shift) -> Unit) ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.doctor_work_times_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = currentList[position]

        holder.apply {
            date.text = item.date
            day.text = item.day
        }

        val childAdapter = WorkTimeItemAdapter()
        childAdapter.submitList(item.shifts)
        holder.rvTimes.layoutManager = GridLayoutManager(holder.itemView.context, 2)
        holder.rvTimes.adapter = childAdapter

        holder.btn.setOnClickListener {
            onItemClicked?.invoke(item.shifts!![0]!!)
        }
    }
}

class WorkTimesDiff : DiffUtil.ItemCallback<WorkTime>() {
    override fun areItemsTheSame(oldItem: WorkTime, newItem: WorkTime): Boolean {
        return newItem.shifts == oldItem.shifts
    }

    override fun areContentsTheSame(oldItem: WorkTime, newItem: WorkTime): Boolean {
        return newItem == oldItem
    }
}