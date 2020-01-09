package com.example.belajarmaps.Customer.Fragment


import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.belajarmaps.Admin.Model.Category
import com.example.belajarmaps.Admin.Model.Wisata
import com.example.belajarmaps.Customer.Activity.ListByCategoryAct
import com.example.belajarmaps.Customer.Adapter.CategoryAdapter
import com.example.belajarmaps.Customer.Adapter.WisataAdapter
import com.google.firebase.database.*
import com.example.belajarmaps.R
import es.dmoral.toasty.Toasty

class HomeFragment : Fragment() {

    private lateinit var txt_viewall:TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var backup_category:LottieAnimationView
    private lateinit var backup_paket:LottieAnimationView

    private lateinit var reference : DatabaseReference

    private var mCategory: ArrayList<Category> = arrayListOf()
    private lateinit var categoryAdapter: CategoryAdapter
    lateinit var mWisata: MutableList<Wisata>
    private lateinit var wisataAdapter: WisataAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        txt_viewall = view.findViewById(R.id.txt_viewall)
        backup_category = view.findViewById(R.id.backup_category)
        backup_paket = view.findViewById(R.id.backup_paket)

        recyclerView = view.findViewById(R.id.rv_category)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        getCategory()

        recyclerView2 = view.findViewById(R.id.rv_wisata)
        recyclerView2.setHasFixedSize(true)
        recyclerView2.layoutManager = LinearLayoutManager(context)
        mWisata = mutableListOf()
        getWisata()

        txt_viewall.setOnClickListener {
            startActivity(Intent(activity, ListByCategoryAct::class.java).putExtra("nameofcategory","Semua Destinasi Wisata"))
        }

        return view
    }

    private fun getCategory() {
        reference = FirebaseDatabase.getInstance().reference.child("Category")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    mCategory.clear()
                    for (datasnapshot1: DataSnapshot in dataSnapshot.children) {
                        val p: Category = datasnapshot1.getValue(Category::class.java) as Category
                        mCategory.add(p)
                    }
                    categoryAdapter = context?.let { CategoryAdapter(mCategory, it) }!!
                    recyclerView.adapter = categoryAdapter
                    categoryAdapter.notifyDataSetChanged()

                }else{
                    recyclerView.visibility = View.GONE
                    backup_category.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun getWisata() {
        reference = FirebaseDatabase.getInstance().reference.child("Wisata")
        reference.limitToFirst(2).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    mWisata.clear()
                    for (datasnapshot1: DataSnapshot in dataSnapshot.children) {
                        val p: Wisata = datasnapshot1.getValue(Wisata::class.java) as Wisata
                        mWisata.add(p)
                    }
                    wisataAdapter = context?.let { WisataAdapter(mWisata, it) }!!
                    recyclerView2.adapter = wisataAdapter
                    wisataAdapter.notifyDataSetChanged()

                }else{
                    txt_viewall.visibility = View.GONE
                    recyclerView2.visibility = View.GONE
                    backup_paket.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

}
