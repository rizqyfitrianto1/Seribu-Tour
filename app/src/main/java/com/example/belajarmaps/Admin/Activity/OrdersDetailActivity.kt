package com.example.belajarmaps.Admin.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.example.belajarmaps.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import java.text.NumberFormat
import java.util.*

class OrdersDetailActivity : AppCompatActivity() {

    private lateinit var img_wisata: ImageView
    private lateinit var tv_nama_wisata: TextView
    private lateinit var tv_date: TextView
    private lateinit var tv_qty: TextView
    private lateinit var tv_total_harga: TextView
    private lateinit var tv_nama: TextView
    private lateinit var tv_no_hp: TextView
    private lateinit var tv_email: TextView
    private lateinit var text_barcode_number : TextView

    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders_detail)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Detail Ticket"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val key = intent.getStringExtra("key")

        img_wisata = findViewById(R.id.img_wisata)
        tv_nama_wisata = findViewById(R.id.tv_nama_wisata)
        tv_date = findViewById(R.id.tv_date)
        tv_qty = findViewById(R.id.tv_qty)
        tv_total_harga = findViewById(R.id.tv_total_harga)
        tv_nama = findViewById(R.id.tv_nama)
        tv_no_hp = findViewById(R.id.tv_no_hp)
        tv_email = findViewById(R.id.tv_email)
        text_barcode_number = findViewById(R.id.text_barcode_number)

        getData(key!!)

    }

    private fun getData( key: String) {
        reference = FirebaseDatabase.getInstance().reference.child("Order").child(key)
        reference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    val nama_wisata = dataSnapshot.child("nama_wisata").value.toString()
                    val date =  dataSnapshot.child("date").value.toString()
                    val qty =  dataSnapshot.child("jumlah_tiket").value.toString()
                    val total =  dataSnapshot.child("total_harga").value.toString()
                    val nama =  dataSnapshot.child("nama").value.toString()
                    val no_hp =  dataSnapshot.child("no_hp").value.toString()
                    val email =  dataSnapshot.child("email").value.toString()
                    val id_ticket =  dataSnapshot.child("id_ticket").value.toString()
                    val img =  dataSnapshot.child("img_wisata").value.toString()

                    tv_nama_wisata.text = nama_wisata
                    tv_date.text = date
                    tv_qty.text = qty
                    tv_nama.text = nama
                    tv_no_hp.text = no_hp
                    tv_email.text = email
                    text_barcode_number.text = id_ticket

                    Picasso.get()
                        .load(img)
                        .centerCrop()
                        .fit()
                        .into(img_wisata)

                    val localeID = Locale("in", "ID")
                    val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
                    tv_total_harga.text = formatRupiah.format(total.toDouble())

                }else{
                    Toasty.error(applicationContext,"No Data Found", Toasty.LENGTH_SHORT,true).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}
