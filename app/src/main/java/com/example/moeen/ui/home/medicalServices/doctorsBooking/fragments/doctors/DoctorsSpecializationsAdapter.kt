package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments.doctors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.marginStart
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moeen.R
import com.example.moeen.network.model.specializationsResponse.SpecializationsResponse.Data
import com.example.moeen.utils.dpToPx
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class DoctorsSpecializationsAdapter(private val context: Context) : RecyclerView.Adapter<DoctorsSpecializationsAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.tvDoctorsSpecialization)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Data>(){
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)
    var specializationsList : MutableList<Data>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    var onItemClicked : ((Data) -> Unit) ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.doctors_specialization_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = specializationsList[position]
        holder.name.text = item.name

        // TODO: Fix the first item margin
        if(position == 0){
            val value = holder.itemView.marginStart
            val param = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(value, 0, dpToPx(16, context), 0)
            holder.itemView.layoutParams = param
        }

        if(item.checked){
            holder.itemView.setBackgroundResource(R.drawable._10_blue_rect)
            holder.name.setTextColor(context.getColor(R.color.white))
        }

        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(item)
        }
    }

    override fun getItemCount(): Int = specializationsList.size
}