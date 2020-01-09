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
import es.dmoral.toasty.Toasty
import com.example.belajarmaps.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class CategoryAddActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var nameEditText:EditText
    private lateinit var img_category: ImageView
    private lateinit var edt_lat:EditText
    private lateinit var edt_lon:EditText
    private lateinit var saveBtn:Button
    private lateinit var cekMap:Button
    private lateinit var loading:RelativeLayout

    private lateinit var reference: DatabaseReference
    private lateinit var storage: StorageReference

    private var photo_location: Uri? = null
    private var PHOTO_MAX: Int = 1

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_add)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Add Category"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        nameEditText = findViewById(R.id.nameEditText)
        img_category = findViewById(R.id.img_category)
        edt_lat = findViewById(R.id.edt_lat)
        edt_lon = findViewById(R.id.edt_lon)
        saveBtn = findViewById(R.id.saveBtn)
        cekMap = findViewById(R.id.cekMap)
        loading = findViewById(R.id.loading)

        img_category.setOnClickListener {
            findPhoto()
        }

        cekMap.setOnClickListener {
            val lat = edt_lat.text.toString()
            val lon = edt_lon.text.toString()

            if (lat.isEmpty() || lon.isEmpty()){
                Toasty.warning(this, "Masukkan lat dan lon", Toasty.LENGTH_SHORT,true).show()
            }else{
                val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }

        saveBtn.setOnClickListener {
            saveBtn.isEnabled = false
            saveBtn.text = "Loading..."
            loading.visibility = View.VISIBLE

            val name = nameEditText.text.toString()
            val lat = edt_lat.text.toString()
            val lon = edt_lon.text.toString()

            if ( name.isEmpty() || lat.isEmpty() || lon.isEmpty()){
                saveBtn.isEnabled = true
                saveBtn.text = "Save"
                loading.visibility = View.GONE
                Toasty.warning(this, "All fill required", Toasty.LENGTH_SHORT,true).show()
            }else{
                saveCategory( name, lat, lon)
            }

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val name = nameEditText.text.toString()
        val lat = edt_lat.text.toString()
        val lon = edt_lon.text.toString()
        val latD = lat.toDouble()
        val lonD = lon.toDouble()
        val location = LatLng(latD, lonD)
        mMap.addMarker(MarkerOptions().position(location).title(name))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,15f))
    }

    private fun saveCategory(name: String, lat: String, lon: String) {
        reference = FirebaseDatabase.getInstance().reference.child("Category")
        storage = FirebaseStorage.getInstance().reference.child("Photocategory").child(name)

        if (photo_location != null){

            val mStorageReference: StorageReference = storage.child( System.currentTimeMillis().toString() + "." + getFileExtension(
                photo_location!!
            ))

            mStorageReference.putFile(photo_location!!)
                .addOnFailureListener {

                }
                .addOnSuccessListener {
                    mStorageReference.downloadUrl.addOnSuccessListener { uri ->
                        val id_category = reference.push().key.toString()
                        reference.child(id_category).ref.child("image").setValue(uri.toString())
                        reference.child(id_category).ref.child("id_category").setValue(id_category)
                        reference.child(id_category).ref.child("name").setValue(name)
                        reference.child(id_category).ref.child("lat").setValue(lat)
                        reference.child(id_category).ref.child("lon").setValue(lon)

                        nameEditText.setText("")
                        edt_lat.setText("")
                        edt_lon.setText("")
                    }
                }.addOnCompleteListener {
                    loading.visibility = View.GONE
                    saveBtn.isEnabled = true
                    saveBtn.text = "Save"
                    Toasty.success(this,"Data Inserted", Toasty.LENGTH_SHORT, true).show()
                    finish()
                }
        }else{
            loading.visibility = View.GONE
            saveBtn.isEnabled = true
            saveBtn.text = "Save"
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
                .into(img_category)

        }
    }

    fun getFileExtension(uri: Uri): String? {
        val contentResolver: ContentResolver = contentResolver
        val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

}
