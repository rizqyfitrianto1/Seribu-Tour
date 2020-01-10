package com.example.belajarmaps.Customer.Fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.belajarmaps.Customer.Adapter.TicketAdapter
import com.example.belajarmaps.Customer.Model.Ticket

import com.example.belajarmaps.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class TicketUsedFragment : Fragment() {

    private lateinit var ll_no_ticket: LinearLayout
    private lateinit var recyclerView: RecyclerView

    private lateinit var reference : DatabaseReference
    internal var firebaseUser: FirebaseUser? = null

    lateinit var mTicket: MutableList<Ticket>
    private lateinit var ticketAdapter: TicketAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_ticket_used, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        val userid = firebaseUser?.uid.toString()

        ll_no_ticket = view.findViewById(R.id.ll_no_ticket)
        recyclerView = view.findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        mTicket = mutableListOf()
        getTicket(userid)

        return view
    }

    private fun getTicket(userid: String) {
        reference = FirebaseDatabase.getInstance().reference.child("MyTicket").child(userid)
        reference.orderByChild("status").equalTo("Sudah Digunakan")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()) {
                        ll_no_ticket.visibility = View.GONE
                        mTicket.clear()
                        for (datasnapshot1: DataSnapshot in dataSnapshot.children) {
                            val p: Ticket = datasnapshot1.getValue(Ticket::class.java) as Ticket
                            mTicket.add(p)
                        }
                        ticketAdapter = context?.let { TicketAdapter(mTicket, it) }!!
                        recyclerView.adapter = ticketAdapter
                        ticketAdapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
    }

}
