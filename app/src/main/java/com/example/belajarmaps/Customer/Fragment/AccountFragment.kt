package com.example.belajarmaps.Customer.Fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.belajarmaps.Customer.Activity.EditProfileActivity
import com.example.belajarmaps.Customer.Activity.FAQActivity
import com.example.belajarmaps.Customer.Activity.OurOfficeActivity
import com.example.belajarmaps.GetStartedActivity
import com.example.belajarmaps.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class AccountFragment : Fragment() {

    internal lateinit var view: View
    private lateinit var tv_edit : TextView
    private lateinit var btn_signout : Button
    private lateinit var photo_profile : ImageView
    private lateinit var tv_nama_lengkap:TextView
    private lateinit var tv_no_hp:TextView
    private lateinit var tv_email:TextView
    private lateinit var tv_name:TextView

    private lateinit var ll_our_office:LinearLayout
    private lateinit var ll_faq:LinearLayout

    private lateinit var reference : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_account, container, false)

        tv_edit = view.findViewById(R.id.tv_edit)
        btn_signout = view.findViewById(R.id.btn_sign_out)
        photo_profile = view.findViewById(R.id.photo_profile)
        tv_nama_lengkap = view.findViewById(R.id.tv_nama_lengkap)
        tv_no_hp = view.findViewById(R.id.tv_no_hp)
        tv_email = view.findViewById(R.id.tv_email)
        tv_name = view.findViewById(R.id.tv_name)

        ll_our_office = view.findViewById(R.id.ll_our_office)
        ll_faq = view.findViewById(R.id.ll_faq)

        getDataUser()

        tv_edit.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }

        ll_faq.setOnClickListener {
            startActivity(Intent(activity, FAQActivity::class.java))
        }


        ll_our_office.setOnClickListener {
            startActivity(Intent(activity, OurOfficeActivity::class.java))
        }

        btn_signout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity, GetStartedActivity::class.java))
            activity?.finish()
        }

        return view
    }

    private fun getDataUser(){
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
