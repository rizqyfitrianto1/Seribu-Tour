package com.example.belajarmaps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView

class GetStartedActivity : AppCompatActivity() {

    private lateinit var btn_sign_in : Button
    private lateinit var btn_new_account_create : Button
    private lateinit var ttb : Animation
    private lateinit var intro_app : TextView
    private lateinit var btt : Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        //load element
        btn_sign_in = findViewById(R.id.btn_sign_in)
        btn_new_account_create = findViewById(R.id.btn_new_account_create)
        intro_app = findViewById(R.id.intro_app)

        //load animation
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb)
        btt = AnimationUtils.loadAnimation(this, R.anim.btt)

        //start animation
        intro_app.startAnimation(ttb)
        btn_sign_in.startAnimation(btt)
        btn_new_account_create.startAnimation(btt)

        //menuju halaman sign in
        btn_sign_in.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        })

        //menuju halaman new account / register
        btn_new_account_create.setOnClickListener(View.OnClickListener {
            startActivity ( Intent(this, RegisterActivity::class.java))
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val exit = Intent(Intent.ACTION_MAIN)
        exit.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(exit)
        finish()
    }
}
