package com.example.belajarmaps.Admin.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.belajarmaps.Admin.Adapter.MyPagerAdapter
import com.example.belajarmaps.Admin.Adapter.OrdersAdapter
import com.example.belajarmaps.Customer.Adapter.TicketAdapter
import com.example.belajarmaps.Customer.Model.Ticket
import com.example.belajarmaps.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_orders.*

class OrdersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Orders"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        view_pager.adapter = MyPagerAdapter(supportFragmentManager)
        tab_layout.setupWithViewPager(view_pager)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.order_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.query_menu -> {
                startActivity(Intent(this, QueryAndSearchAct::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }
}
