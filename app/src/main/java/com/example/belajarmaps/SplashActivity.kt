package com.example.belajarmaps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.belajarmaps.Admin.Activity.HomeAdminActivity
import com.example.belajarmaps.Customer.Activity.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class SplashActivity : AppCompatActivity() {

    internal var firebaseUser: FirebaseUser? = null
    private lateinit var database: DatabaseReference

    override fun onStart() {
        super.onStart()

        firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null) {
            database = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val email = dataSnapshot.child("email").value.toString()
                        if (email == "admin@gmail.com"){

                            database.child("photo").addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        handler = Handler()
                                        handler.postDelayed({
                                            startActivity(Intent(this@SplashActivity, HomeAdminActivity::class.java))
                                            finish()
                                        }, 2500)
                                    } else {
                                        handler = Handler()
                                        handler.postDelayed({
                                        startActivity(Intent(this@SplashActivity, RegisterTwoActivity::class.java))
                                        finish()
                                        }, 2500)
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {

                                }
                            })
                        }else {
                            database.child("photo").addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        handler = Handler()
                                        handler.postDelayed({
                                            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                                            finish()
                                        }, 2500)
                                    } else {
                                        handler = Handler()
                                        handler.postDelayed({
                                        startActivity(Intent(this@SplashActivity, RegisterTwoActivity::class.java))
                                        finish()
                                        }, 2500)
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {

                                }
                            })
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }else{
            handler = Handler()
            handler.postDelayed({
                startActivity(Intent(this@SplashActivity, GetStartedActivity::class.java))
                finish()
            }, 2500)
        }
    }

    private lateinit var app_splash : Animation
    private lateinit var btt : Animation
    private lateinit var handler: Handler
    private lateinit var app_logo : ImageView
    private lateinit var app_subtitle : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash)
        btt = AnimationUtils.loadAnimation(this, R.anim.btt)

        app_logo = findViewById(R.id.app_logo)
        app_subtitle = findViewById(R.id.app_subtitle)

        app_logo.startAnimation(app_splash)
        app_subtitle.startAnimation(btt)
    }
}
