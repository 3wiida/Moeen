package com.example.moeen.ui.home.homeAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginStart
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moeen.R
import com.example.moeen.network.model.homeResponse.Service
import com.example.moeen.utils.dpToPx

class HomeAdapter1(private val context: Context) : RecyclerView.Adapter<HomeAdapter1.MyViewHolder>() {

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(R.id.ivHomeItem1)
        val title : TextView = itemView.findViewById(R.id.tvHomeItem1)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Service>(){
        override fun areItemsTheSame(oldItem: Service, newItem: Service): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Service, newItem: Service): Boolean {
            return oldItem == newItem
        }
    }
    var onItemClick: ((Int)-> Unit)?=null
    private val differ = AsyncListDiffer(this, differCallback)

    var homeList1 : List<Service>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.home_item_template1, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = homeList1[position]
        holder.apply {
            Glide.with(holder.itemView).load(item.photo).into(image)
            title.text = item.name
        }
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(position)
        }

        if(position == 0){
            val value = holder.itemView.marginStart
            val param = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(value, value, dpToPx(16, context), value)
            holder.itemView.layoutParams = param
        }
    }

    override fun getItemCount(): Int {
        return homeList1.size
    }
}

//recycler_brands.addItemDecoration(new LinearSpacingItemDecoration(Common.dpToPx(15,this)))