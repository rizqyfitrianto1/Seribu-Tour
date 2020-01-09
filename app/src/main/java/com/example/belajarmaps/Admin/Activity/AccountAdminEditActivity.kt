package com.example.belajarmaps.Admin.Activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.appcompat.widget.Toolbar
import com.example.belajarmaps.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import es.dmoral.toasty.Toasty

class AccountAdminEditActivity : AppCompatActivity() {

    private lateinit var photo_profile: CircleImageView
    private lateinit var edt_nama: EditText
    private lateinit var edt_no_telp: EditText
    private lateinit var btn_save: Button
    private lateinit var btn_add_photo: Button
    private lateinit var loading: RelativeLayout

    private lateinit var reference: DatabaseReference
    private lateinit var storage: StorageReference

    private var photo_location: Uri? = null
    private var PHOTO_MAX: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_admin_edit)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Edit Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        btn_save = findViewById(R.id.btn_save)
        btn_add_photo = findViewById(R.id.btn_add_photo)
        photo_profile = findViewById(R.id.photo_profile)
        edt_nama = findViewById(R.id.edt_nama)
        edt_no_telp = findViewById(R.id.edt_no_telp)
        loading = findViewById(R.id.loading)

        val firebaseUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        reference = FirebaseDatabase.getInstance().reference.child("Users")
        reference.child(firebaseUser).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val photo = p0.child("photo").value.toString()
                    val nama = p0.child("fullname").value.toString()
                    val no_hp = p0.child("no_telp").value.toString()

                    edt_nama.setText(nama)
                    edt_no_telp.setText(no_hp)
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

        btn_add_photo.setOnClickListener {
            findPhoto()
        }

        val user = FirebaseAuth.getInstance().currentUser?.uid.toString()
        reference = FirebaseDatabase.getInstance().reference.child("Users").child(user)
        storage = FirebaseStorage.getInstance().reference.child("Photousers").child(user)

        btn_save.setOnClickListener {
            btn_save.isEnabled = false
            btn_save.text = "Loading ..."
            loading.visibility = View.VISIBLE

            val name = edt_nama.text.toString()
            val no_telp = edt_no_telp.text.toString()

            if (name.isEmpty() || no_telp.isEmpty()){
                btn_save.isEnabled = true
                btn_save.text = "Save"
                loading.visibility = View.GONE
                Toasty.warning(this,"All fill required", Toasty.LENGTH_SHORT,true).show()
            }else{
                savePhoto( name, no_telp)
            }
        }
    }

    private fun savePhoto(name: String, no_telp: String) {
        if(photo_location != null){
            val mStorageReference: StorageReference = storage.child( System.currentTimeMillis().toString() + "." + getFileExtension(
                photo_location!!
            ))

            mStorageReference.putFile(photo_location!!)
                .addOnFailureListener {

                }
                .addOnSuccessListener {

                    mStorageReference.downloadUrl.addOnSuccessListener { uri ->
                        reference.ref.child("photo").setValue(uri.toString())
                        reference.ref.child("fullname").setValue(name)
                        reference.ref.child("no_telp").setValue(no_telp)
                    }
                }.addOnCompleteListener {
                    loading.visibility = View.GONE
                    Toasty.info(this, "Berhasil update profile", Toasty.LENGTH_SHORT,true).show()
                    finish()
                }
        }else{
            loading.visibility = View.GONE
            reference.ref.child("fullname").setValue(name)
            reference.ref.child("no_telp").setValue(no_telp)
            Toasty.info(this, "Berhasil update profile", Toasty.LENGTH_SHORT,true).show()
            finish()
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
                .into(photo_profile)

        }
    }

    fun getFileExtension(uri: Uri): String? {
        val contentResolver: ContentResolver = contentResolver
        val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }
}
