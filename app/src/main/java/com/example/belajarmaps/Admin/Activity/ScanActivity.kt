package com.example.belajarmaps.Admin.Activity

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.belajarmaps.Customer.Model.Ticket
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import com.example.belajarmaps.R
import java.text.NumberFormat
import java.util.*

class ScanActivity : AppCompatActivity() {

    private lateinit var img_wisata: ImageView
    private lateinit var tv_nama_wisata: TextView
    private lateinit var tv_date: TextView
    private lateinit var tv_qty: TextView
    private lateinit var tv_total_harga: TextView
    private lateinit var tv_nama: TextView
    private lateinit var tv_no_hp: TextView
    private lateinit var tv_email: TextView
    private lateinit var text_barcode_number: TextView
    private lateinit var btn_status: Button

    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        val code = intent.getStringExtra("code")

        img_wisata = findViewById(R.id.img_wisata)
        tv_nama_wisata = findViewById(R.id.tv_nama_wisata)
        tv_date = findViewById(R.id.tv_date)
        tv_qty = findViewById(R.id.tv_qty)
        tv_total_harga = findViewById(R.id.tv_total_harga)
        tv_nama = findViewById(R.id.tv_nama)
        tv_no_hp = findViewById(R.id.tv_no_hp)
        tv_email = findViewById(R.id.tv_email)
        text_barcode_number = findViewById(R.id.text_barcode_number)
        btn_status = findViewById(R.id.btn_status)

        getData(code)

        btn_status.setOnClickListener {
            btn_status.isEnabled = false
            btn_status.text = "Sudah Digunakan"
            btn_status.background.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
            reference = FirebaseDatabase.getInstance().reference.child("Order")
            reference.orderByChild("id_ticket").equalTo(code).addListenerForSingleValueEvent(object:
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()) {
                        for (datasnapshot1: DataSnapshot in dataSnapshot.children) {
                            val key = datasnapshot1.getValue(Ticket::class.java)?.key
                            val userid = datasnapshot1.getValue(Ticket::class.java)?.user

                            reference = FirebaseDatabase.getInstance().reference.child("Order").child(key.toString())
                            reference.ref.child("status").setValue("Sudah Digunakan").addOnCompleteListener {
                                reference = FirebaseDatabase.getInstance().reference.child("MyTicket").child(userid!!).child(key.toString())
                                reference.ref.child("status").setValue("Sudah Digunakan")
                            }

                            val handler = Handler()
                            handler.postDelayed({
                                startActivity(Intent(this@ScanActivity, HomeAdminActivity::class.java))
                                finish()
                            }, 1000)
                        }
                    }else{
                        Toasty.error(applicationContext,"No Data Found", Toasty.LENGTH_SHORT,true).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }
    }

    private fun getData(code: String?) {
        reference = FirebaseDatabase.getInstance().reference.child("Order")
        reference.orderByChild("id_ticket").equalTo(code).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (datasnapshot1: DataSnapshot in dataSnapshot.children) {
                        val nama_wisata = datasnapshot1.getValue(Ticket::class.java)?.nama_wisata
                        val id_wisata = datasnapshot1.getValue(Ticket::class.java)?.id_wisata
                        val date = datasnapshot1.getValue(Ticket::class.java)?.date
                        val qty = datasnapshot1.getValue(Ticket::class.java)?.jumlah_tiket
                        val total = datasnapshot1.getValue(Ticket::class.java)?.total_harga
                        val nama = datasnapshot1.getValue(Ticket::class.java)?.nama
                        val no_hp = datasnapshot1.getValue(Ticket::class.java)?.no_hp
                        val email = datasnapshot1.getValue(Ticket::class.java)?.email

                        tv_nama_wisata.text = nama_wisata
                        tv_date.text = date
                        tv_qty.text = qty
                        tv_nama.text = nama
                        tv_no_hp.text = no_hp
                        tv_email.text = email
                        text_barcode_number.text = code

                        val localeID = Locale("in", "ID")
                        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
                        tv_total_harga.text = formatRupiah.format(total?.toDouble())

                        reference = FirebaseDatabase.getInstance().reference.child("Wisata").child(id_wisata!!)
                        reference.addListenerForSingleValueEvent(object: ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val photoLink: String = dataSnapshot.child("image").value.toString()
                                Picasso.get()
                                    .load(photoLink)
                                    .centerCrop()
                                    .fit()
                                    .into(img_wisata)
                            }

                            override fun onCancelled(databaseError: DatabaseError) {

                            }
                        })
                    }
                }else{
                    Toasty.error(applicationContext,"No Data Found", Toasty.LENGTH_SHORT,true).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}
