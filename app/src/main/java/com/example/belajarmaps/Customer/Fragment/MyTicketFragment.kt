package com.example.belajarmaps.Customer.Fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.belajarmaps.Admin.Adapter.MyPagerAdapter
import com.example.belajarmaps.Customer.Adapter.MyPagerAdapterCst
import com.example.belajarmaps.Customer.Adapter.TicketAdapter
import com.example.belajarmaps.Customer.Model.Ticket
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.example.belajarmaps.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_orders.*

class MyTicketFragment : Fragment() {

    private lateinit var view_pager: ViewPager
    private lateinit var tab_layout: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_ticket, container, false)

        view_pager = view.findViewById(R.id.view_pager)
        tab_layout = view.findViewById(R.id.tab_layout)

        view_pager.adapter = MyPagerAdapterCst(childFragmentManager)
        tab_layout.setupWithViewPager(view_pager)

        return view
    }
}
