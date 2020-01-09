package com.example.belajarmaps.Customer.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.belajarmaps.Admin.Model.Wisata
import com.example.belajarmaps.R
import com.squareup.picasso.Picasso

class RateAdapter(val mWisata :List<Wisata>, val context: Context) : RecyclerView.Adapter<RateAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false))
    }

    override fun getItemCount(): Int {
        return mWisata.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val wisata = mWisata[position]
        holder.nama.text = wisata.nama
        holder.date.text = wisata.date
        holder.rate.rating = wisata.rate.toFloat()
        holder.comment.text = wisata.comment

        Picasso.get()
            .load(wisata.photo)
            .centerCrop()
            .fit()
            .into(holder.image)

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var date : TextView = itemView.findViewById(R.id.tv_date)
        var nama : TextView = itemView.findViewById(R.id.tv_nama)
        var rate : RatingBar = itemView.findViewById(R.id.rb_ticket)
        var image : ImageView = itemView.findViewById(R.id.img_user)
        var comment : TextView = itemView.findViewById(R.id.tv_comment)

    }

}