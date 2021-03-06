package com.example.belajarmaps.Customer.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.belajarmaps.Admin.Model.Category
import com.example.belajarmaps.Admin.Model.Wisata
import com.example.belajarmaps.Customer.Activity.DetailWisataActivity
import com.example.belajarmaps.Customer.Model.User
import com.example.belajarmaps.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*

class WisataAdapter(val mWisata :List<Wisata>, val context:Context) : RecyclerView.Adapter<WisataAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wisata, parent, false))
    }

    override fun getItemCount(): Int {
        return mWisata.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val wisata = mWisata[position]

        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        holder.tv_harga.text = formatRupiah.format(wisata.harga.toDouble()) + " / pack"

        holder.tv_judul.text = wisata.judul
        holder.tv_durasi.text = wisata.durasi
        holder.tv_lokasi.text = wisata.lokasi

        val img = wisata.image

        Picasso.get()
            .load(img)
            .centerCrop()
            .fit()
            .into(holder.img_wisata)

        holder.itemView.setOnClickListener{

            val reference = FirebaseDatabase.getInstance().reference.child("Category")
            reference.orderByChild("name").equalTo(wisata.lokasi).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists()) {
                        for (datasnapshot1: DataSnapshot in p0.children) {
                            val category: Category = datasnapshot1.getValue(Category::class.java) as Category
                            val lat = category.lat
                            val lon = category.lon

                            val intent = Intent(context, DetailWisataActivity::class.java)
                            intent.putExtra("img", img)
                            intent.putExtra("id_wisata", wisata.id_wisata)
                            intent.putExtra("judul", wisata.judul)
                            intent.putExtra("durasi", wisata.durasi)
                            intent.putExtra("lokasi", wisata.lokasi)
                            intent.putExtra("harga", wisata.harga)
                            intent.putExtra("deskripsi", wisata.deskripsi)
                            intent.putExtra("lat", lat)
                            intent.putExtra("lon", lon)
                            context.startActivity(intent)
                        }
                    }
                }
                override fun onCancelled(p0: DatabaseError) {

                }
            })
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var img_wisata : ImageView = itemView.findViewById(R.id.img_wisata)
        var tv_judul : TextView = itemView.findViewById(R.id.tv_judul)
        var tv_durasi : TextView = itemView.findViewById(R.id.tv_durasi)
        var tv_lokasi : TextView = itemView.findViewById(R.id.tv_lokasi)
        var tv_harga : TextView = itemView.findViewById(R.id.tv_harga)

    }

}