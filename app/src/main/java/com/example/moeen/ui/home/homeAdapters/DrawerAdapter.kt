package com.example.moeen.ui.home.homeAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moeen.R
import com.example.moeen.ui.home.RecyclerItem

class DrawerAdapter : RecyclerView.Adapter<DrawerAdapter.MyViewHolder>() {

    var onItemClicked : ((RecyclerItem) -> Unit) ?= null

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.tvDrawerItem)
        val image : ImageView = itemView.findViewById(R.id.ivDrawerItem)


    }

    private val differCallback = object : DiffUtil.ItemCallback<RecyclerItem>(){
        override fun areItemsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
            return newItem.title == oldItem.title
        }

        override fun areContentsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
            return newItem == oldItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)

    var drawerList : List<RecyclerItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.drawer_menu_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = drawerList[position]

        holder.apply {
            title.text = item.title
            if (item.image != null) {
                image.setImageResource(item.image)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(item)
        }
    }

    override fun getItemCount(): Int = drawerList.size
}