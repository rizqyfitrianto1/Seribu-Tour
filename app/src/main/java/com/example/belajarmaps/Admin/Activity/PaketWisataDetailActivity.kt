package com.example.belajarmaps.Admin.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.belajarmaps.Admin.Model.Wisata
import com.example.belajarmaps.Customer.Activity.FormPesananActivity
import com.example.belajarmaps.Customer.Adapter.RateAdapter
import com.example.belajarmaps.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*

class PaketWisataDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var img_wisata: ImageView
    private lateinit var tv_judul: TextView
    private lateinit var tv_durasi: TextView
    private lateinit var tv_deskripsi: TextView
    private lateinit var tv_lokasi: TextView
    private lateinit var tv_harga: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tv_rate_num: TextView
    private lateinit var rb_ticket: RatingBar
    private lateinit var tv_total_user: TextView

    private lateinit var ll_first: LinearLayout
    private lateinit var ll: LinearLayout
    private lateinit var arrowBtn: ImageView
    private lateinit var view_rate:View
    private lateinit var tv_rate:TextView

    private lateinit var reference : DatabaseReference

    lateinit var mWisata: MutableList<Wisata>
    private lateinit var rateAdapter: RateAdapter

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paket_wisata_detail)

        img_wisata  = findViewById(R.id.img_wisata)
        tv_judul  = findViewById(R.id.tv_judul)
        tv_durasi  = findViewById(R.id.tv_durasi)
        tv_deskripsi  = findViewById(R.id.tv_deskripsi)
        tv_lokasi  = findViewById(R.id.tv_lokasi)
        tv_harga  = findViewById(R.id.tv_harga)
        tv_rate_num  = findViewById(R.id.tv_rate_num)
        rb_ticket  = findViewById(R.id.rb_ticket)
        tv_total_user  = findViewById(R.id.tv_total_user)

        ll_first  = findViewById(R.id.ll_first)
        ll = findViewById(R.id.ll)
        arrowBtn = findViewById(R.id.arrowBtn)
        view_rate = findViewById(R.id.view_rate)
        tv_rate = findViewById(R.id.tv_rate)

        val img = intent.getStringExtra("img")
        val judul = intent.getStringExtra("judul")
        val durasi = intent.getStringExtra("durasi")
        val lokasi = intent.getStringExtra("lokasi")
        val harga = intent.getStringExtra("harga")
        val deskripsi = intent.getStringExtra("deskripsi")

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = judul
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        Picasso.get()
            .load(img)
            .centerCrop()
            .fit()
            .into(img_wisata)

        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        tv_harga.text = formatRupiah.format(harga.toDouble())

        tv_judul.text = judul
        tv_durasi.text = durasi
        tv_deskripsi.text = deskripsi
        tv_lokasi.text = lokasi

        recyclerView = findViewById(R.id.rv_review)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        mWisata = mutableListOf()
        getRate(judul)

        ll_first.setOnClickListener{
            if (recyclerView.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(ll, AutoTransition())
                recyclerView.visibility = View.VISIBLE
                arrowBtn.setBackgroundResource(R.drawable.ic_arrow_down_blue)
            } else {
                TransitionManager.beginDelayedTransition(ll, AutoTransition())
                recyclerView.visibility = View.GONE
                arrowBtn.setBackgroundResource(R.drawable.ic_arrow_right_blue)
            }
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val lokasi = intent.getStringExtra("lokasi")
        val lat = intent.getStringExtra("lat").toDouble()
        val lon = intent.getStringExtra("lon").toDouble()
        val location = LatLng(lat, lon)
        mMap.addMarker(MarkerOptions().position(location).title(lokasi))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,15f))
    }

    private fun getRate(judul: String?) {
        reference = FirebaseDatabase.getInstance().reference
            .child("Wisata").child(judul!!).child("Rate")
        reference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    mWisata.clear()
                    var total_rate = 0f
                    for (datasnapshot1: DataSnapshot in dataSnapshot.children) {
                        val p: Wisata = datasnapshot1.getValue(Wisata::class.java) as Wisata
                        mWisata.add(p)

                        val t_rate = p.rate.toFloat()
                        total_rate += t_rate
                    }
                    rateAdapter = RateAdapter(mWisata, this@PaketWisataDetailActivity)
                    recyclerView.adapter = rateAdapter
                    rateAdapter.notifyDataSetChanged()

                    val t_user = recyclerView.adapter!!.itemCount
                    val total_user = t_user.toString()
                    tv_total_user.text = "$total_user reviews"

                    val average_rate = (total_rate/t_user)
//                    val df = DecimalFormat("#.##")
//                    tv_rate_num.text = df.format(average_rate)
                    tv_rate_num.text = average_rate.toString()

                    rb_ticket.rating = average_rate

                }else{
                    tv_rate.visibility = View.GONE
                    ll.visibility = View.GONE
                    view_rate.visibility = View.GONE
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}
