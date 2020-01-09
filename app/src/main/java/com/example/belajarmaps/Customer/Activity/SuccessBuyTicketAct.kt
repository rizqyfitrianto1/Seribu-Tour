package com.example.belajarmaps.Customer.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.belajarmaps.R

class SuccessBuyTicketAct : AppCompatActivity() {

    private lateinit var btnViewTicket : Button
    private lateinit var btt : Animation
    private lateinit var ttb : Animation
    private lateinit var app_splash : Animation
    private lateinit var icon_succcess_ticket : ImageView
    private lateinit var app_title : TextView
    private lateinit var app_subtitle : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_buy_ticket)

        //Load Element
        btnViewTicket = findViewById(R.id.btn_view_ticket)
        icon_succcess_ticket = findViewById(R.id.icon_success_ticket)
        app_title = findViewById(R.id.app_title)
        app_subtitle = findViewById(R.id.app_subtitle)

        //Load Animation
        btt = AnimationUtils.loadAnimation(this, R.anim.btt)
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash)
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb)


        //start Animation
        icon_succcess_ticket.startAnimation(app_splash)

        app_title.startAnimation(ttb)
        app_subtitle.startAnimation(ttb)

        btnViewTicket.startAnimation(btt)

        btnViewTicket.setOnClickListener{
            startActivity( Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}
