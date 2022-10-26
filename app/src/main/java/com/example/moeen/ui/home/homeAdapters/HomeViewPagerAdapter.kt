package com.example.moeen.ui.home.homeAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.moeen.R
import com.example.moeen.network.model.homeResponse.Slider

class HomeViewPagerAdapter : RecyclerView.Adapter<HomeViewPagerAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(R.id.ivHomePager)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Slider>(){
        override fun areItemsTheSame(oldItem: Slider, newItem: Slider): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Slider, newItem: Slider): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)

    var list : List<Slider>
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
            Glide.with(itemView).load(item.photo).transform(CenterInside(), RoundedCorners(50)).into(image)
        }
    }

    override fun getItemCount(): Int = list.size
}