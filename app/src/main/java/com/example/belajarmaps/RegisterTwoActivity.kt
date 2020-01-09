package com.example.belajarmaps

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_register_two.*

class RegisterTwoActivity : AppCompatActivity() {

    private lateinit var btnContinue: Button
    private lateinit var imgPicPhotoUser: ImageView
    private lateinit var btnAddPhoto: Button
    private lateinit var edtNamaLengkap: EditText
    private lateinit var edtNoTelp: EditText
    private lateinit var loading:RelativeLayout

    private lateinit var reference: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var firebaseUser: FirebaseUser

    private var photo_location: Uri? = null
    private var PHOTO_MAX: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_two)

        btnContinue = findViewById(R.id.btn_continue)
        btnAddPhoto = findViewById(R.id.btn_add_photo)
        imgPicPhotoUser = findViewById(R.id.pict_photo_register_user)
        edtNamaLengkap = findViewById(R.id.edt_nama_lengkap)
        edtNoTelp = findViewById(R.id.edt_no_telp)
        loading = findViewById(R.id.loading)

        btnAddPhoto.setOnClickListener {
            findPhoto()
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val user = firebaseUser.uid
        reference = FirebaseDatabase.getInstance().reference.child("Users").child(user)
        storage = FirebaseStorage.getInstance().reference.child("Photousers").child(user)

        btnContinue.setOnClickListener {
            btnContinue.isEnabled = false
            btnContinue.text = "Loading ..."
            loading.visibility = View.VISIBLE

            val name = edtNamaLengkap.text.toString()
            val no_telp = edtNoTelp.text.toString()

            if (name.isEmpty() || no_telp.isEmpty()){
                btn_continue.isEnabled = true
                btn_continue.text = "Continue"
                loading.visibility = View.GONE
                Toasty.warning(this,"All fill required", Toasty.LENGTH_SHORT,true).show()
            }else{
                savePhoto( name, no_telp)
            }
        }
    }

    private fun savePhoto(fullname: String, no_telp: String) {
        if(photo_location != null){
            val mStorageReference: StorageReference = storage.child( System.currentTimeMillis().toString() + "." + getFileExtension(
                photo_location!!
            ))

            mStorageReference.putFile(photo_location!!)
                .addOnFailureListener {
                    loading.visibility = View.GONE
                }
                .addOnSuccessListener {

                    reference.ref.child("fullname").setValue(fullname)
                    reference.ref.child("no_telp").setValue(no_telp)

                    mStorageReference.downloadUrl.addOnSuccessListener { uri ->
                        reference.ref.child("photo").setValue(uri.toString())
                    }
                }.addOnCompleteListener {
                    startActivity( Intent(this, SuccessRegisterActivity::class.java))
                    loading.visibility = View.GONE
                    finish()
                }
        }else{
            btnContinue.isEnabled = true
            btnContinue.text = "Continue"
            loading.visibility = View.GONE
            Toasty.warning(this, "Please add your photo", Toast.LENGTH_SHORT, true).show()

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
                .into(imgPicPhotoUser)

        }
    }

    fun getFileExtension(uri: Uri): String? {
        val contentResolver: ContentResolver = contentResolver
        val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }
}
