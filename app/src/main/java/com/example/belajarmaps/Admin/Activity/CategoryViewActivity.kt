package com.example.belajarmaps.Admin.Activity

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.belajarmaps.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso

class CategoryViewActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var tv_lat:TextView
    private lateinit var tv_lon:TextView
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_view)

        tv_lat = findViewById(R.id.tv_lat)
        tv_lon = findViewById(R.id.tv_lon)

        val name = intent.getStringExtra("name")
        val lat = intent.getStringExtra("lat")
        val lon = intent.getStringExtra("lon")

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = name
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        tv_lat.text = "Lat : $lat"
        tv_lon.text = "Lon : $lon"

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val lokasi = intent.getStringExtra("name")
        val lat = intent.getStringExtra("lat").toDouble()
        val lon = intent.getStringExtra("lon").toDouble()
        val location = LatLng(lat, lon)
        mMap.addMarker(MarkerOptions().position(location).title(lokasi))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,15f))
    }
}
