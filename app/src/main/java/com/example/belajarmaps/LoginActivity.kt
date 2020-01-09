package com.example.belajarmaps

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.airbnb.lottie.LottieAnimationView
import com.example.belajarmaps.Admin.Activity.HomeAdminActivity
import com.example.belajarmaps.Customer.Activity.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import es.dmoral.toasty.Toasty

class LoginActivity : AppCompatActivity() {

    private lateinit var btnNewAccount: TextView
    private lateinit var btnForgotPassword: TextView
    private lateinit var btnSignIn: Button
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var loading: RelativeLayout

    private lateinit var reference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    internal var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnNewAccount = findViewById(R.id.btn_new_account)
        btnForgotPassword = findViewById(R.id.btn_forgot)
        btnSignIn = findViewById(R.id.btn_sign_in)
        edtEmail = findViewById(R.id.email)
        edtPassword = findViewById(R.id.password)
        loading = findViewById(R.id.loading)

        firebaseAuth = FirebaseAuth.getInstance()
        reference = FirebaseDatabase.getInstance().getReference("Users")

        btnNewAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        btnSignIn.setOnClickListener {
            btnSignIn.isEnabled = false
            btnSignIn.text = "Loading ..."
            loading.visibility = View.VISIBLE

            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                btnSignIn.isEnabled = true
                btnSignIn.text = "SIGN IN"
                loading.visibility = View.GONE

                Toasty.warning(this, "Please fill all required", Toasty.LENGTH_SHORT, true).show()
            } else if (password.length < 6) {
                btnSignIn.isEnabled = true
                btnSignIn.text = "SIGN IN"
                loading.visibility = View.GONE

                Toasty.warning(this, "Password must be at least 6 characters", Toasty.LENGTH_SHORT, true)
                    .show()
            } else {
                signIn(email, password)
            }
        }
    }

    private fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful){
                    btnSignIn.isEnabled = true
                    btnSignIn.text = "SIGN IN"
                    loading.visibility = View.GONE

                    Toasty.error(
                        this,
                        "Gagal login karena " + it.exception!!.message,
                        Toast.LENGTH_LONG,
                        true
                    ).show()
                }else{
                    firebaseUser = FirebaseAuth.getInstance().currentUser
                    if (email == "admin@gmail.com"){
                        reference.child(firebaseUser!!.uid).child("photo").addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                        startActivity(Intent(this@LoginActivity, HomeAdminActivity::class.java))
                                        finish()
                                } else {
                                        startActivity(Intent(this@LoginActivity, RegisterTwoActivity::class.java))
                                        finish()
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {

                            }
                        })
                    }else{
                        reference.child(firebaseUser!!.uid).child("photo").addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                                    finish()
                                } else {
                                    startActivity(Intent(this@LoginActivity, RegisterTwoActivity::class.java))
                                    finish()
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {

                            }
                        })
                    }
                }
            }
    }
}
