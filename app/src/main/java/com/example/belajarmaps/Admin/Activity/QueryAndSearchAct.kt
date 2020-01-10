package com.example.belajarmaps.Admin.Activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.belajarmaps.Admin.Adapter.OrdersAdapter
import com.example.belajarmaps.Customer.Model.Ticket
import com.example.belajarmaps.R
import com.google.firebase.database.*
import es.dmoral.toasty.Toasty
import java.text.NumberFormat
import java.time.Month
import java.util.*

class QueryAndSearchAct : AppCompatActivity() {

    private lateinit var pick_date:ImageView
    private lateinit var date:TextView
    private lateinit var tv_item:TextView
    private lateinit var tv_ticket:TextView
    private lateinit var tv_price:TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var loading: RelativeLayout

    private lateinit var reference : DatabaseReference

    lateinit var mTicket: MutableList<Ticket>
    private lateinit var ordersAdapter: OrdersAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_and_search)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Query And Search"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        pick_date = findViewById(R.id.pick_date)
        date = findViewById(R.id.date)
        tv_item = findViewById(R.id.tv_item)
        tv_ticket = findViewById(R.id.tv_ticket)
        tv_price = findViewById(R.id.tv_price)
        loading = findViewById(R.id.loading)

        reference = FirebaseDatabase.getInstance().getReference("Order")

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        mTicket = mutableListOf()

        val getDate = intent.getStringExtra("date")
        date.text = getDate
        if (getDate != null){
            getDateQuery(getDate)
        }

        pick_date.setOnClickListener {
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, mYear, mMonth, mDay ->
                val getDate =  mDay.toString() + " " + Month.of(mMonth + 1) + " " + mYear
                date.text = getDate
                getDateQuery(getDate)
            }, year, month,day
            )
            dpd.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //Memanggil/Memasang menu item pada toolbar dari layout menu_bar.xml
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        val searchIem = menu.findItem(R.id.search)
        val searchView = searchIem.actionView as androidx.appcompat.widget.SearchView
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            @SuppressLint("SetTextI18n")
            override fun onQueryTextSubmit(query: String): Boolean {
                getQuery(query)
                date.text = ""
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
        return true
    }


    private fun getDateQuery(date: String) {
        loading.visibility = View.VISIBLE
        reference.orderByChild("date").equalTo(date).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    recyclerView.visibility = View.VISIBLE
                    loading.visibility = View.GONE
                    mTicket.clear()
                    var tot_ticket = 0
                    var tot_price = 0
                    for (datasnapshot1: DataSnapshot in dataSnapshot.children) {
                        val p: Ticket = datasnapshot1.getValue(Ticket::class.java) as Ticket
                        mTicket.add(p)

                        tot_ticket += p.jumlah_tiket.toInt()
                        tot_price += p.total_harga.toInt()
                    }
                    ordersAdapter = OrdersAdapter(mTicket, this@QueryAndSearchAct)
                    recyclerView.adapter = ordersAdapter
                    ordersAdapter.notifyDataSetChanged()

                    val tot_item = recyclerView.adapter!!.itemCount

                    tv_item.text = tot_item.toString()
                    tv_ticket.text = tot_ticket.toString()
                    val localeID = Locale("in", "ID")
                    val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
                    tv_price.text = formatRupiah.format(tot_price.toDouble())

                } else {
                    tv_item.text = ""
                    tv_ticket.text = ""
                    tv_price.text = ""
                    recyclerView.visibility = View.GONE
                    loading.visibility = View.GONE
                    Toasty.error(applicationContext, "No Data Found", Toasty.LENGTH_SHORT, true).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun getQuery(searchText: String) {
        loading.visibility = View.VISIBLE
        val query = searchText.toUpperCase()
        val firebasQuery: Query = reference.orderByChild("id_ticket").startAt(query).endAt(query + "\uf8ff")
        firebasQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    recyclerView.visibility = View.VISIBLE
                    loading.visibility = View.GONE
                    mTicket.clear()
                    var tot_ticket = 0
                    var tot_price = 0
                    for (datasnapshot1: DataSnapshot in dataSnapshot.children) {
                        val p: Ticket = datasnapshot1.getValue(Ticket::class.java) as Ticket
                        mTicket.add(p)

                        tot_ticket += p.jumlah_tiket.toInt()
                        tot_price += p.total_harga.toInt()
                    }
                    ordersAdapter = OrdersAdapter(mTicket, this@QueryAndSearchAct)
                    recyclerView.adapter = ordersAdapter
                    ordersAdapter.notifyDataSetChanged()

                    val tot_item = recyclerView.adapter!!.itemCount

                    tv_item.text = tot_item.toString()
                    tv_ticket.text = tot_ticket.toString()
                    val localeID = Locale("in", "ID")
                    val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
                    tv_price.text = formatRupiah.format(tot_price.toDouble())
                } else {
                    tv_item.text = ""
                    tv_ticket.text = ""
                    tv_price.text = ""
                    recyclerView.visibility = View.GONE
                    loading.visibility = View.GONE
                    Toasty.error(applicationContext, "No Data Found", Toasty.LENGTH_SHORT, true).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

}
