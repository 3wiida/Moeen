package com.example.moeen.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moeen.R

class HomeAdapter2 : RecyclerView.Adapter<HomeAdapter2.MyViewHolder>() {

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(R.id.ivHomeItem2)
        val title : TextView = itemView.findViewById(R.id.tvHomeItem2)
    }

    private val differCallback = object : DiffUtil.ItemCallback<HomeItem1>(){
        override fun areItemsTheSame(oldItem: HomeItem1, newItem: HomeItem1): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: HomeItem1, newItem: HomeItem1): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)

    var homeList2 : List<HomeItem1>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.home_item_template2, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = homeList2[position]
        holder.apply {
            image.setImageResource(item.image)
            title.text = item.title
        }
    }

    override fun getItemCount(): Int {
        return homeList2.size
    }
}