package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moeen.R
import com.example.moeen.common.Constants.TAG
import com.example.moeen.network.model.paymentMethodsResponse.PaymentMethodsResponse.Data
import kotlin.math.log

class PaymentMethodsAdapter (private val itemLayout:Int): ListAdapter<Data, PaymentMethodsAdapter.MyViewHolder>(PaymentDiffUtils()){

    var onItemClicked:((Data) -> Unit) ?=null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val rb : RadioButton = itemView.findViewById(R.id.rbPaymentItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodsAdapter.MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PaymentMethodsAdapter.MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.rb.text = item.name
        holder.rb.setOnClickListener{
            onItemClicked?.invoke(item)
            for(i in currentList){
                i?.isChecked = item.id == i?.id
            }
            if(item?.isChecked == true){
                Log.d(TAG, "onBindViewHolder: should be blue")
                holder.rb.setBackgroundResource(R.drawable._10_null_blue_rect)
            }else{
                Log.d(TAG, "onBindViewHolder: should be gray")
                holder.rb.setBackgroundResource(R.drawable._10_gray_rect)
            }
        }
    }



}

class PaymentDiffUtils : DiffUtil.ItemCallback<Data>(){
    override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
        return oldItem == newItem
    }
}