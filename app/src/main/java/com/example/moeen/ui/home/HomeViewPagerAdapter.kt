package com.example.moeen.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moeen.R

class HomeViewPagerAdapter : RecyclerView.Adapter<HomeViewPagerAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(R.id.ivHomePager)
    }

    private val differCallback = object : DiffUtil.ItemCallback<PagerItem>(){
        override fun areItemsTheSame(oldItem: PagerItem, newItem: PagerItem): Boolean {
            return oldItem.image == newItem.image
        }

        override fun areContentsTheSame(oldItem: PagerItem, newItem: PagerItem): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)

    var list : List<PagerItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.home_pager_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]

        holder.apply {
            image.setImageResource(item.image)
        }
    }

    override fun getItemCount(): Int = list.size
}