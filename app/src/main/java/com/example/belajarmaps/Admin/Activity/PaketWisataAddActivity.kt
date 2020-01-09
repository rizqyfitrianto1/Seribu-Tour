package com.example.belajarmaps.Admin.Activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import com.example.belajarmaps.R

class PaketWisataAddActivity : AppCompatActivity() {

    private lateinit var sp: Spinner
    private lateinit var img_wisata:ImageView
    private lateinit var edt_judul:EditText
    private lateinit var edt_durasi:EditText
    private lateinit var edt_harga:EditText
    private lateinit var edt_deskripsi:EditText
    private lateinit var btn_save:Button

    private lateinit var reference: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var listener:ValueEventListener
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var spinnerDataList:ArrayList<String>
    private lateinit var loading: RelativeLayout

    private var photo_location: Uri? = null
    private var PHOTO_MAX: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paket_wisata_add)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Add Paket Wisata"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        img_wisata = findViewById(R.id.img_wisata)
        edt_judul = findViewById(R.id.edt_judul)
        edt_durasi = findViewById(R.id.edt_durasi)
        edt_harga = findViewById(R.id.edt_harga)
        edt_deskripsi = findViewById(R.id.edt_deskripsi)
        btn_save = findViewById(R.id.btn_save)
        sp = findViewById(R.id.sp)
        loading = findViewById(R.id.loading)

        reference = FirebaseDatabase.getInstance().reference.child("Category")
        spinnerDataList = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerDataList)
        sp.adapter = adapter
        retrieveData()

        img_wisata.setOnClickListener {
            findPhoto()
        }

        btn_save.setOnClickListener {
            btn_save.isEnabled = false
            btn_save.text = "Loading..."
            loading.visibility = View.VISIBLE

            val nama = edt_judul.text.toString()
            val judul = nama.toUpperCase()
            val lokasi = sp.selectedItem.toString()
            val durasi = edt_durasi.text.toString()
            val harga = edt_harga.text.toString()
            val deskripsi = edt_deskripsi.text.toString()

            if ( judul.isEmpty() || lokasi.isEmpty() || durasi.isEmpty() || harga.isEmpty() || deskripsi.isEmpty()){
                btn_save.isEnabled = true
                btn_save.text = "Save"
                loading.visibility = View.GONE
                Toasty.warning(this, "All fill required", Toasty.LENGTH_SHORT,true).show()
            }else{
                saveWiata( judul, lokasi, durasi, harga, deskripsi)
            }
        }
    }

    private fun retrieveData() {
        listener = reference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                for (datasnapshot1 : DataSnapshot in p0.children){
                    val nama = datasnapshot1.child("name").value.toString()
                    spinnerDataList.add(nama)
                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

    private fun saveWiata(judul: String, lokasi: String, durasi: String, harga: String, deskripsi: String) {
        reference = FirebaseDatabase.getInstance().reference.child("Wisata")
        storage = FirebaseStorage.getInstance().reference.child("Photowisata").child(judul)

        if (photo_location != null){

            val mStorageReference: StorageReference = storage.child( System.currentTimeMillis().toString() + "." + getFileExtension(
                photo_location!!
            ))

            mStorageReference.putFile(photo_location!!)
                .addOnFailureListener {

                }
                .addOnSuccessListener {
                    mStorageReference.downloadUrl.addOnSuccessListener { uri ->
                        val id_wisata = reference.push().key.toString()
                        reference.child(id_wisata).ref.child("image").setValue(uri.toString())
                        reference.child(id_wisata).ref.child("id_wisata").setValue(id_wisata)
                        reference.child(id_wisata).ref.child("judul").setValue(judul)
                        reference.child(id_wisata).ref.child("lokasi").setValue(lokasi)
                        reference.child(id_wisata).ref.child("durasi").setValue(durasi)
                        reference.child(id_wisata).ref.child("harga").setValue(harga)
                        reference.child(id_wisata).ref.child("deskripsi").setValue(deskripsi)
                    }
                }.addOnCompleteListener {
                    loading.visibility = View.GONE
                    Toasty.success(this, "Success add data", Toasty.LENGTH_SHORT, true).show()
                    finish()
                }
        }else{
            loading.visibility = View.GONE
            btn_save.isEnabled = true
            btn_save.text = "Save"
            Toasty.warning(this, "Image harus diisi", Toasty.LENGTH_SHORT,true).show()
        }
    }

    private fun findPhoto() {
        val pictureIntent = Intent()
        pictureIntent.type = "image/*"
        pictureIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(pictureIntent, PHOTO_MAX)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PHOTO_MAX && resultCode == Activity.RESULT_OK && data != null && data.data != null){

            photo_location = data.data!!
            Picasso.get()
                .load(photo_location)
                .centerCrop()
                .fit()
                .into(img_wisata)

        }
    }

    fun getFileExtension(uri: Uri): String? {
        val contentResolver: ContentResolver = contentResolver
        val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }
}
