package com.example.belajarmaps.Admin.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.belajarmaps.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import es.dmoral.toasty.Toasty

class FAQAdminAddActivity : AppCompatActivity() {

    private lateinit var et_question:EditText
    private lateinit var et_answer:EditText
    private lateinit var btn_save:Button
    private lateinit var loading: RelativeLayout

    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_faq)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Add FAQ"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        et_question = findViewById(R.id.et_question)
        et_answer = findViewById(R.id.et_answer)
        btn_save = findViewById(R.id.btn_save)
        loading = findViewById(R.id.loading)

        reference = FirebaseDatabase.getInstance().getReference("FAQ")

        btn_save.setOnClickListener {
            btn_save.isEnabled = false
            btn_save.text  = "Loading..."
            loading.visibility = View.VISIBLE
            val question = et_question.text.toString()
            val answer = et_answer.text.toString()

            if(question.isEmpty() || answer.isEmpty()){
                btn_save.isEnabled = true
                btn_save.text  = "Save"
                loading.visibility = View.GONE
                Toasty.warning(this, "All fill required", Toasty.LENGTH_SHORT, true).show()
            }else{
                saveFAQ(question, answer)
            }
        }
    }

    private fun saveFAQ(question: String, answer: String) {
        val id = reference.push().key.toString()
        val faq = HashMap<String, String>()
        faq["id"] = id
        faq["question"] = question
        faq["answer"] = answer
        reference.child(id).setValue(faq).addOnCompleteListener {
            btn_save.isEnabled = true
            btn_save.text  = "Save"
            loading.visibility = View.GONE
            Toasty.success(this, "Berhasil menambahkan data", Toasty.LENGTH_SHORT,true).show()
            finish()
        }
    }
}
