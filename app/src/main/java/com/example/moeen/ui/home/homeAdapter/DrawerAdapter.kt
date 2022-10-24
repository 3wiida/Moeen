package com.example.moeen.ui.home.homeAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moeen.R
import com.example.moeen.ui.home.DrawerMenuItem

class DrawerAdapter : RecyclerView.Adapter<DrawerAdapter.MyViewHolder>() {

    var onItemClicked : ((DrawerMenuItem) -> Unit) ?= null

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.tvDrawerItem)
        val image : ImageView = itemView.findViewById(R.id.ivDrawerItem)


    }

    private val differCallback = object : DiffUtil.ItemCallback<DrawerMenuItem>(){
        override fun areItemsTheSame(oldItem: DrawerMenuItem, newItem: DrawerMenuItem): Boolean {
            return newItem.title == oldItem.title
        }

        override fun areContentsTheSame(oldItem: DrawerMenuItem, newItem: DrawerMenuItem): Boolean {
            return newItem == oldItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)

    var drawerList : List<DrawerMenuItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.drawer_menu_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = drawerList[position]
        holder.title.text = item.title
        holder.image.setImageResource(item.image)

        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(item)
        }
    }

    override fun getItemCount(): Int = drawerList.size
}