package com.example.belajarmaps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import es.dmoral.toasty.Toasty
import java.util.HashMap

class RegisterActivity : AppCompatActivity() {

    private lateinit var edt_password:EditText
    private lateinit var edt_email_address:EditText
    private lateinit var btn_continue:Button
    private lateinit var loading: RelativeLayout

    private lateinit var reference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edt_password = findViewById(R.id.edt_password)
        edt_email_address = findViewById(R.id.edt_email_address)
        btn_continue = findViewById(R.id.btn_continue)
        loading = findViewById(R.id.loading)

        reference = FirebaseDatabase.getInstance().reference.child("Users")
        firebaseAuth = FirebaseAuth.getInstance()

        btn_continue.setOnClickListener {
            btn_continue.isEnabled = false
            btn_continue.text = "Loading..."
            loading.visibility = View.VISIBLE

            val email = edt_email_address.text.toString()
            val password = edt_password.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                btn_continue.isEnabled = true
                btn_continue.text = "Continue"
                loading.visibility = View.GONE
                Toasty.warning(this,"All fill required",Toasty.LENGTH_SHORT,true).show()
            }else if(password.length<6){
                btn_continue.isEnabled = true
                btn_continue.text = "Continue"
                loading.visibility = View.GONE
                Toasty.warning(this, "Password must be at least 6 characters", Toasty.LENGTH_SHORT, true).show()
            }else{
                saveAuth(email, password)
            }
        }
    }

    private fun saveAuth(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    btn_continue.isEnabled = true
                    btn_continue.text = "Continue"
                    loading.visibility = View.GONE
                    Toasty.error(
                        this,
                        "Register gagal karena " + it.exception!!.message,
                        Toast.LENGTH_LONG,
                        true
                    ).show()
                } else {
                    saveUser(email, password)
                }
            }
    }

    private fun saveUser(email: String, password: String) {
        val hashMap = HashMap<String, String>()
        hashMap["email"] = email
        hashMap["password"] = password

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val user = firebaseUser.uid

        reference.child(user).setValue(hashMap)
            .addOnCompleteListener {
                if(!it.isSuccessful){
                    btn_continue.isEnabled = true
                    btn_continue.text = "Continue"
                    loading.visibility = View.GONE
                    Toasty.error(
                        this,
                        "Register gagal karena " + it.exception!!.message,
                        Toast.LENGTH_LONG,
                        true
                    ).show()
                }else{
                    startActivity(Intent(this, RegisterTwoActivity::class.java))
                    loading.visibility = View.GONE
                    finish()
                }
            }
    }

}
