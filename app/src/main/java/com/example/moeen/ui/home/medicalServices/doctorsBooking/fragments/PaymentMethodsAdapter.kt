package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moeen.R
import com.example.moeen.network.model.paymentMethodsResponse.PaymentMethodsResponse.Data

class PaymentMethodsAdapter : ListAdapter<Data, PaymentMethodsAdapter.MyViewHolder>(PaymentDiffUtils()){

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val rb : RadioButton = itemView.findViewById(R.id.rbPaymentItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodsAdapter.MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.payment_method_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PaymentMethodsAdapter.MyViewHolder, position: Int) {
        val item = currentList[position]

        holder.rb.text = item.name
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