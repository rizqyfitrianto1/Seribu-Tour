package com.example.belajarmaps.Admin.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.belajarmaps.Customer.Activity.EditProfileActivity
import com.example.belajarmaps.GetStartedActivity
import com.example.belajarmaps.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class AccountAdminActivity : AppCompatActivity() {

    private lateinit var tv_edit : TextView
    private lateinit var btn_sign_out : Button
    private lateinit var photo_profile : ImageView
    private lateinit var tv_nama_lengkap: TextView
    private lateinit var tv_no_hp: TextView
    private lateinit var tv_email: TextView
    private lateinit var tv_name: TextView

    private lateinit var reference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_account)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Account"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        tv_edit = findViewById(R.id.tv_edit)
        photo_profile = findViewById(R.id.photo_profile)
        tv_nama_lengkap = findViewById(R.id.tv_nama_lengkap)
        tv_no_hp = findViewById(R.id.tv_no_hp)
        tv_email = findViewById(R.id.tv_email)
        tv_name = findViewById(R.id.tv_name)
        btn_sign_out = findViewById(R.id.btn_sign_out)

        getDataUser()

        tv_edit.setOnClickListener {
            startActivity(Intent(this, AccountAdminEditActivity::class.java))
        }

        btn_sign_out.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, GetStartedActivity::class.java))
            finish()
        }
    }

    private fun getDataUser() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        reference = FirebaseDatabase.getInstance().reference.child("Users")
        reference.child(firebaseUser).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val photo = p0.child("photo").value.toString()
                    val nama = p0.child("fullname").value.toString()
                    val no_hp = p0.child("no_telp").value.toString()
                    val email = p0.child("email").value.toString()

                    tv_nama_lengkap.text = nama
                    tv_no_hp.text = no_hp
                    tv_email.text = email
                    tv_name.text = nama
                    Picasso.get()
                        .load(photo)
                        .centerCrop()
                        .fit()
                        .into(photo_profile)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}
