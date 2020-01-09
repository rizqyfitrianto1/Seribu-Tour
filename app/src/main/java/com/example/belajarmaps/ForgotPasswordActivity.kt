package com.example.belajarmaps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import es.dmoral.toasty.Toasty

class ForgotPasswordActivity : AppCompatActivity() {

    internal lateinit var firebaseAuth: FirebaseAuth
    private lateinit var btn_reset:Button
    private lateinit var send_email:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        send_email = findViewById(R.id.send_email)
        btn_reset = findViewById(R.id.btn_reset)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Forgot Password"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        firebaseAuth = FirebaseAuth.getInstance()

        btn_reset.setOnClickListener {
            val email = send_email.text.toString()

            if (email.equals("")){
                Toasty.warning(this, "All fields are required!", Toasty.LENGTH_SHORT,true).show()
            }else{
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toasty.info(this, "Please check your Email", Toasty.LENGTH_SHORT,true).show()
                        finish()
                    }else{
                        val error = it.exception!!.message.toString()
                        Toasty.error(this, error, Toasty.LENGTH_SHORT,true).show()
                    }
                }
            }
        }
    }
}
