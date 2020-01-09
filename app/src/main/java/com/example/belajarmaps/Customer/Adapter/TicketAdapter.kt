package com.example.belajarmaps.Customer.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.belajarmaps.Customer.Activity.DetailTicketActivity
import com.example.belajarmaps.Customer.Model.Ticket
import com.example.belajarmaps.R

class TicketAdapter(val mTicket :List<Ticket>, val context: Context) : RecyclerView.Adapter<TicketAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_ticket, parent, false))
    }

    override fun getItemCount(): Int {
        return mTicket.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ticket = mTicket[position]
        holder.nama_wisata.text = ticket.nama_wisata
        holder.date.text = ticket.date
        holder.jumlah_tiket.text = ticket.jumlah_tiket + " Tiket"
        holder.status.text = ticket.status

        if(ticket.status == "Sudah Digunakan"){
            holder.status.background.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
        }

        holder.itemView.setOnClickListener{
            val intent = Intent(context, DetailTicketActivity::class.java)
            intent.putExtra("id_wisata",ticket.id_wisata)
            intent.putExtra("key",ticket.key)
            intent.putExtra("id_ticket",ticket.id_ticket)
            intent.putExtra("nama_wisata",ticket.nama_wisata)
            context.startActivity(intent)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nama_wisata : TextView = itemView.findViewById(R.id.tv_nama_wisata)
        var date : TextView = itemView.findViewById(R.id.tv_date)
        var jumlah_tiket : TextView = itemView.findViewById(R.id.tv_jumlah_tiket)
        var status : TextView = itemView.findViewById(R.id.tv_status)

    }

}