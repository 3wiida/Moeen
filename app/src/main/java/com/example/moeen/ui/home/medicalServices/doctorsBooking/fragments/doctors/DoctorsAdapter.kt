package com.example.moeen.ui.home.medicalServices.doctorsBooking.fragments.doctors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moeen.R
import com.example.moeen.network.model.doctorsResponse.DoctorsResponse.Data
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text

class DoctorsAdapter : RecyclerView.Adapter<DoctorsAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.tvDoctorName)
        val image : CircleImageView = itemView.findViewById(R.id.ivDoctorImage)
        val rating : RatingBar = itemView.findViewById(R.id.doctorRating)
        val specialization : TextView = itemView.findViewById(R.id.tvDoctorSpecialization)
        val location : TextView = itemView.findViewById(R.id.tvDoctorLocation)
        val price : TextView = itemView.findViewById(R.id.tvDoctorSessionPrice)
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

    var doctorsList : List<Data>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.doctor_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = doctorsList[position]

        holder.apply {
            name.text = item.name
            rating.rating = item.rate.toFloat()
            specialization.text = item.specialtyId.name
            location.text = item.address
            price.text = item.medicalPrice.toString()
            Glide.with(itemView).load(item.photo).into(image)
        }
    }

    override fun getItemCount(): Int = doctorsList.size
}