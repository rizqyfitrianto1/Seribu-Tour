package com.example.belajarmaps.Customer.Activity

import android.app.Dialog
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.ColorInt
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_detail_ticket.*
import com.example.belajarmaps.R
import java.text.DateFormat
import java.text.NumberFormat
import java.util.*

class DetailTicketActivity : AppCompatActivity() {

    private lateinit var img_barcode:ImageView
    private lateinit var img_wisata:ImageView
    private lateinit var tv_nama_wisata:TextView
    private lateinit var tv_date:TextView
    private lateinit var tv_qty:TextView
    private lateinit var tv_total_harga:TextView
    private lateinit var tv_nama:TextView
    private lateinit var tv_no_hp:TextView
    private lateinit var tv_email:TextView
    private lateinit var tv_status:TextView
    private lateinit var cv_rate:CardView
    private lateinit var rt_ticket:RatingBar
    private lateinit var et_comment:EditText
    private lateinit var btn_save:Button
    private lateinit var tv_close:TextView

    private lateinit var reference: DatabaseReference

    internal lateinit var dialog: Dialog
    internal lateinit var inflater: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_ticket)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Detail Ticket"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val userid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val key = intent.getStringExtra("key")
        val id_ticket = intent.getStringExtra("id_ticket")
        val nama_wisata = intent.getStringExtra("nama_wisata")
        val id_wisata = intent.getStringExtra("id_wisata")

        img_barcode = findViewById(R.id.img_barcode)
        img_wisata = findViewById(R.id.img_wisata)
        tv_nama_wisata = findViewById(R.id.tv_nama_wisata)
        tv_date = findViewById(R.id.tv_date)
        tv_qty = findViewById(R.id.tv_qty)
        tv_total_harga = findViewById(R.id.tv_total_harga)
        tv_nama = findViewById(R.id.tv_nama)
        tv_no_hp = findViewById(R.id.tv_no_hp)
        tv_email = findViewById(R.id.tv_email)
        tv_status = findViewById(R.id.tv_status)
        cv_rate = findViewById(R.id.cv_rate)

        displayBitmap(id_ticket!!)
        getData(id_wisata,userid,key!!)

        cv_rate.setOnClickListener {
            rateNow(id_wisata,nama_wisata,key)
        }
    }

    private fun rateNow(id_wisata: String?, nama_wisata: String?, key: String?) {
        dialog = Dialog(this)
        inflater = layoutInflater
        dialog.setContentView(R.layout.layout_rate)
        dialog.setCancelable(true)

        rt_ticket = dialog.findViewById(R.id.rt_ticket)
        et_comment = dialog.findViewById(R.id.et_comment)
        btn_save = dialog.findViewById(R.id.btn_save)
        tv_close = dialog.findViewById(R.id.tv_close)

        tv_close.setOnClickListener {
            dialog.dismiss()
        }

        btn_save.setOnClickListener {

            val calendar = Calendar.getInstance()
            val localeID = Locale("in", "ID")
            val df = DateFormat.getDateInstance(DateFormat.LONG, localeID)
            val date = df.format(calendar.time)
            val rate = rt_ticket.rating.toString()
            val comment = et_comment.text.toString()

            if (rate.isEmpty() ) {
                Toasty.warning(applicationContext, "Berikan rating dan review anda",
                    Toasty.LENGTH_SHORT, true).show()
            } else {
                val firebaseUser = FirebaseAuth.getInstance().currentUser!!
                val userid = firebaseUser.uid

                reference = FirebaseDatabase.getInstance().reference.child("Users").child(userid)
                reference.addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        if(p0.exists()) {
                            val nama = p0.child("fullname").value.toString()
                            val photo = p0.child("photo").value.toString()

                            val hashMap = HashMap<String, String>()
                            hashMap["date"] = date
                            hashMap["rate"] = rate
                            hashMap["comment"] = comment
                            hashMap["nama"] = nama
                            hashMap["photo"] = photo

                            reference = FirebaseDatabase.getInstance().reference.child("Wisata")
                                .child(id_wisata!!)
                                .child("Rate")
                            reference.child(key!!).setValue(hashMap).addOnCompleteListener {
                                dialog.dismiss()
                                cv_rate.visibility = View.GONE
                            }
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun getData(id_wisata:String, userid: String, key: String) {
        reference = FirebaseDatabase.getInstance().reference.child("MyTicket").child(userid).child(key)
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
                        val status =  dataSnapshot.child("status").value.toString()
                        val img =  dataSnapshot.child("img_wisata").value.toString()

                        tv_nama_wisata.text = nama_wisata
                        tv_date.text = date
                        tv_qty.text = qty
                        tv_nama.text = nama
                        tv_no_hp.text = no_hp
                        tv_email.text = email

                        Picasso.get()
                        .load(img)
                        .centerCrop()
                        .fit()
                        .into(img_wisata)

                        val localeID = Locale("in", "ID")
                        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
                        tv_total_harga.text = formatRupiah.format(total.toDouble())

                        if(status == "Sudah Digunakan"){
                            tv_status.visibility = View.VISIBLE
                            reference = FirebaseDatabase.getInstance().reference.child("Wisata").child(id_wisata)
                            reference.addListenerForSingleValueEvent(object :ValueEventListener{
                                override fun onDataChange(p0: DataSnapshot) {
                                    if (p0.exists()){
                                        reference.child("Rate").child(key).addListenerForSingleValueEvent(object :ValueEventListener{
                                            override fun onDataChange(p0: DataSnapshot) {
                                                if(p0.exists()) {
                                                    cv_rate.visibility = View.GONE
                                                }else{
                                                    cv_rate.visibility = View.VISIBLE
                                                }
                                            }
                                            override fun onCancelled(p0: DatabaseError) {

                                            }
                                        })

                                    }
                                }
                                override fun onCancelled(p0: DatabaseError) {

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

    private fun displayBitmap(value: String) {
        val widthPixels = resources.getDimensionPixelSize(R.dimen.width_barcode)
        val heightPixels = resources.getDimensionPixelSize(R.dimen.height_barcode)

        img_barcode.setImageBitmap(
            createBarcodeBitmap(
                barcodeValue = value,
                barcodeColor = getColor(R.color.blackPrimary),
                backgroundColor = getColor(android.R.color.white),
                widthPixels = widthPixels,
                heightPixels = heightPixels
            )
        )
        text_barcode_number.text = value
    }

    private fun createBarcodeBitmap(barcodeValue: String, @ColorInt barcodeColor: Int,
        @ColorInt backgroundColor: Int, widthPixels: Int, heightPixels: Int): Bitmap {
        val bitMatrix = Code128Writer().encode(barcodeValue, BarcodeFormat.CODE_128, widthPixels, heightPixels
        )

        val pixels = IntArray(bitMatrix.width * bitMatrix.height)
        for (y in 0 until bitMatrix.height) {
            val offset = y * bitMatrix.width
            for (x in 0 until bitMatrix.width) {
                pixels[offset + x] =
                    if (bitMatrix.get(x, y)) barcodeColor else backgroundColor
            }
        }

        val bitmap = Bitmap.createBitmap(bitMatrix.width, bitMatrix.height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, bitMatrix.width, 0, 0, bitMatrix.width, bitMatrix.height)

        return bitmap
    }
}
