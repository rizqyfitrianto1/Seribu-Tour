package com.example.belajarmaps.Customer.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.belajarmaps.Admin.Model.FAQ
import com.example.belajarmaps.Customer.Adapter.FAQAdapter
import com.example.belajarmaps.R
import com.google.firebase.database.*
import es.dmoral.toasty.Toasty

class FAQActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var backup_faq:RelativeLayout

    private lateinit var reference : DatabaseReference

    lateinit var mFAQ: MutableList<FAQ>
    private lateinit var faqAdapter: FAQAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)

        backup_faq = findViewById(R.id.backup_faq)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Frequently And Aswers"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        reference = FirebaseDatabase.getInstance().reference.child("FAQ")

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        mFAQ = mutableListOf()
        getFAQ()
    }

    private fun getFAQ() {
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    mFAQ.clear()
                    for (datasnapshot1: DataSnapshot in dataSnapshot.children) {
                        val faq: FAQ = datasnapshot1.getValue(FAQ::class.java) as FAQ
                        mFAQ.add(faq)
                    }
                    faqAdapter = FAQAdapter(mFAQ, this@FAQActivity)
                    recyclerView.adapter = faqAdapter
                    faqAdapter.notifyDataSetChanged()
                } else {
                    recyclerView.visibility = View.GONE
                    backup_faq.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}
