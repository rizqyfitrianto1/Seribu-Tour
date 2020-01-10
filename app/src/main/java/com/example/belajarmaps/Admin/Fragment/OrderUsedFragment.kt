package com.example.belajarmaps.Admin.Fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.belajarmaps.Admin.Adapter.OrdersAdapter
import com.example.belajarmaps.Customer.Model.Ticket

import com.example.belajarmaps.R
import com.google.firebase.database.*

class OrderUsedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var empty: RelativeLayout

    private lateinit var reference : DatabaseReference

    lateinit var mTicket: MutableList<Ticket>
    private lateinit var ordersAdapter: OrdersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_used, container, false)

        empty = view.findViewById(R.id.empty)

        recyclerView = view.findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        mTicket = mutableListOf()
        getOrders()

        return view
    }

    private fun getOrders() {
        reference = FirebaseDatabase.getInstance().reference.child("Order")
        reference.orderByChild("status").equalTo("Sudah Digunakan").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    mTicket.clear()
                    for (datasnapshot1: DataSnapshot in dataSnapshot.children) {
                        val p: Ticket = datasnapshot1.getValue(Ticket::class.java) as Ticket
                        mTicket.add(p)
                    }
                    ordersAdapter = context?.let { OrdersAdapter(mTicket, it) }!!
                    recyclerView.adapter = ordersAdapter
                    ordersAdapter.notifyDataSetChanged()
                }else{
                    empty.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

}
