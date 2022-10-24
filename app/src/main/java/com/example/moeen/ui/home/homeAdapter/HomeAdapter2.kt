package com.example.moeen.ui.home.homeAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.moeen.R
import com.example.moeen.network.model.postsResponse.Data

class HomeAdapter2 : RecyclerView.Adapter<HomeAdapter2.MyViewHolder>() {

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(R.id.ivHomeItem2)
        val title : TextView = itemView.findViewById(R.id.tvHomeItem2)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Data>(){
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)

    var homeList2 : List<Data>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.home_item_template2, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = homeList2[position]
        /*if(position == 0){
            val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(0, 5, 5, 30)
            holder.itemView.layoutParams = layoutParams
        }*/
        holder.apply {
            Glide.with(itemView).load(item.photo).transform(CenterInside(), RoundedCorners(50)).into(image)
            title.text = item.title
        }
    }

    override fun getItemCount(): Int {
        return homeList2.size
    }
}