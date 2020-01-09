package com.example.belajarmaps.Customer.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.belajarmaps.Admin.Model.Wisata
import com.google.firebase.database.*
import es.dmoral.toasty.Toasty
import com.example.belajarmaps.R
import android.annotation.SuppressLint
import android.view.Menu
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.widget.SearchView
import com.example.belajarmaps.Customer.Adapter.WisataAdapter


class ListByCategoryAct : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var reference : DatabaseReference

    lateinit var mWisata: MutableList<Wisata>
    private lateinit var wisataAdapter: WisataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_by_category)

        val name = intent.getStringExtra("nameofcategory")

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = name
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        reference = FirebaseDatabase.getInstance().reference.child("Wisata")

        recyclerView = findViewById(R.id.rv_wisata)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        mWisata = mutableListOf()
        getWisata(name)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //Memanggil/Memasang menu item pada toolbar dari layout menu_bar.xml
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        val searchIem = menu.findItem(R.id.search)
        val searchView = searchIem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @SuppressLint("SetTextI18n")
            override fun onQueryTextSubmit(query: String): Boolean {
                getWisataQuery(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun getWisataQuery(searchText: String) {
        val query = searchText.toUpperCase()
        val firebasQuery: Query = reference.orderByChild("judul").startAt(query).endAt(query + "\uf8ff")
        firebasQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    mWisata.clear()
                    for (datasnapshot1: DataSnapshot in dataSnapshot.children) {
                        val p: Wisata = datasnapshot1.getValue(Wisata::class.java) as Wisata
                        mWisata.add(p)
                    }
                    wisataAdapter = WisataAdapter(mWisata, this@ListByCategoryAct)
                    recyclerView.adapter = wisataAdapter
                    wisataAdapter.notifyDataSetChanged()
                } else {
                    Toasty.error(applicationContext, "No Data Found", Toasty.LENGTH_SHORT, true).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun getWisata(name: String?) {
        if (name != "Semua Destinasi Wisata") {
            reference.orderByChild("lokasi").equalTo(name).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        mWisata.clear()
                        for (datasnapshot1: DataSnapshot in dataSnapshot.children) {
                            val p: Wisata = datasnapshot1.getValue(Wisata::class.java) as Wisata
                            mWisata.add(p)
                        }
                        wisataAdapter = WisataAdapter(mWisata, this@ListByCategoryAct)
                        recyclerView.adapter = wisataAdapter
                        wisataAdapter.notifyDataSetChanged()
                    } else {
                        Toasty.error(applicationContext, "No Data Found", Toasty.LENGTH_SHORT, true).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }else{
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        mWisata.clear()
                        for (datasnapshot1: DataSnapshot in dataSnapshot.children) {
                            val p: Wisata = datasnapshot1.getValue(Wisata::class.java) as Wisata
                            mWisata.add(p)
                        }
                        wisataAdapter = WisataAdapter(mWisata, this@ListByCategoryAct)
                        recyclerView.adapter = wisataAdapter
                        wisataAdapter.notifyDataSetChanged()
                    } else {
                        Toasty.error(applicationContext, "No Data Found", Toasty.LENGTH_SHORT, true).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }
    }
}
