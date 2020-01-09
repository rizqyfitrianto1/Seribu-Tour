package com.example.belajarmaps.Admin.Adapter

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
import com.example.belajarmaps.Admin.Activity.OrdersDetailActivity
import com.example.belajarmaps.Customer.Activity.DetailTicketActivity
import com.example.belajarmaps.Customer.Model.Ticket
import com.example.belajarmaps.R

class OrdersAdapter(val mTicket :List<Ticket>, val context: Context) : RecyclerView.Adapter<OrdersAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false))
    }

    override fun getItemCount(): Int {
        return mTicket.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ticket = mTicket[position]
        holder.id_ticket.text = ticket.id_ticket
        holder.nama_wisata.text = ticket.nama_wisata
        holder.date.text = ticket.date
        holder.jumlah_tiket.text = ticket.jumlah_tiket + " Tiket"
        holder.status.text = ticket.status

        if(ticket.status == "Sudah Digunakan"){
            holder.status.background.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
        }

        holder.itemView.setOnClickListener{
            val intent = Intent(context, OrdersDetailActivity::class.java)
            intent.putExtra("key",ticket.key)
            context.startActivity(intent)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nama_wisata : TextView = itemView.findViewById(R.id.tv_nama_wisata)
        var date : TextView = itemView.findViewById(R.id.tv_date)
        var jumlah_tiket : TextView = itemView.findViewById(R.id.tv_jumlah_tiket)
        var status : TextView = itemView.findViewById(R.id.tv_status)
        var id_ticket : TextView = itemView.findViewById(R.id.tv_id_ticket)

    }

}