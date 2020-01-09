package com.example.belajarmaps.Customer.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.belajarmaps.Customer.Model.User
import com.example.belajarmaps.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*

class OurOfficeActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var tv_no_telp: TextView
    private lateinit var tv_email: TextView

    private lateinit var ll_call_phone: LinearLayout
    private lateinit var ll_call_wa: LinearLayout
    private lateinit var ll_call_email: LinearLayout

    private lateinit var reference: DatabaseReference

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_our_office)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Our Office"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        tv_no_telp = findViewById(R.id.tv_no_telp)
        tv_email = findViewById(R.id.tv_email)

        ll_call_phone = findViewById(R.id.ll_call_phone)
        ll_call_wa = findViewById(R.id.ll_call_wa)
        ll_call_email = findViewById(R.id.ll_call_email)

        reference = FirebaseDatabase.getInstance().reference.child("Users")
        reference.orderByChild("email").equalTo("admin@gmail.com").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    for (datasnapshot1: DataSnapshot in p0.children) {
                        val user: User = datasnapshot1.getValue(User::class.java) as User

                        val no_hp = user.no_telp
                        val email = user.email

                        tv_no_telp.text = no_hp
                        tv_email.text = email
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        ll_call_phone.setOnClickListener {
            val phoneNo = tv_no_telp.text.toString()
            val dial = "tel:$phoneNo"
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(dial)))
        }

        ll_call_wa.setOnClickListener {
            val nomorHp = tv_no_telp.text.toString().substring(1)
            openWhatsApp("+62$nomorHp")
        }

        ll_call_email.setOnClickListener {
            val email = tv_email.text.toString()

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))

            try {
                startActivity(Intent.createChooser(intent, "Ingin Mengirim Email ?"))
            } catch (ex: android.content.ActivityNotFoundException) {
                //do something else
            }

        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun openWhatsApp(number: String) {
        val url = "https://api.whatsapp.com/send?phone=$number"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val location = LatLng(-6.1653935, 106.7647282)
        mMap.addMarker(MarkerOptions().position(location).title("Our Office"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,15f))
    }
}
