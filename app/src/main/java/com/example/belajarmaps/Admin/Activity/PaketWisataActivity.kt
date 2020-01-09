package com.example.belajarmaps.Admin.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.belajarmaps.Admin.Adapter.WisataAdminAdapter
import com.example.belajarmaps.Admin.Model.Wisata
import com.example.belajarmaps.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import es.dmoral.toasty.Toasty

class PaketWisataActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btn_add_wisata:FloatingActionButton
    private lateinit var backup: RelativeLayout

    private lateinit var reference : DatabaseReference

    lateinit var mWisata: MutableList<Wisata>
    private lateinit var wisataAdapter: WisataAdminAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paket_wisata)

        backup = findViewById(R.id.backup)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Paket Wisata"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        btn_add_wisata = findViewById(R.id.btn_add_wisata)

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        mWisata = mutableListOf()
        getWisata()

        btn_add_wisata.setOnClickListener {
            reference = FirebaseDatabase.getInstance().reference.child("Category")
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()){
                        startActivity(Intent(this@PaketWisataActivity, PaketWisataAddActivity::class.java))
                    }else{
                        Toasty.warning(this@PaketWisataActivity, "Create a category first", Toasty.LENGTH_SHORT, true).show()
                    }
                }
                override fun onCancelled(p0: DatabaseError) {

                }
            })
        }
    }

    private fun getWisata() {
        reference = FirebaseDatabase.getInstance().reference.child("Wisata")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    mWisata.clear()
                    for (datasnapshot1: DataSnapshot in dataSnapshot.children) {
                        val p: Wisata = datasnapshot1.getValue(Wisata::class.java) as Wisata
                        mWisata.add(p)
                    }
                    wisataAdapter = WisataAdminAdapter(mWisata, this@PaketWisataActivity)
                    recyclerView.adapter = wisataAdapter
                    wisataAdapter.notifyDataSetChanged()
                }else{
                    recyclerView.visibility = View.GONE
                    backup.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}
