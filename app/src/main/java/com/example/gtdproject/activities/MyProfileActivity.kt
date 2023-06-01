package com.example.gtdproject.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.gtdproject.R
import com.example.gtdproject.firebase.FirestoreClass
import com.example.gtdproject.models.User
import com.example.gtdproject.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException

class MyProfileActivity : BaseActivity() {


    private var mSelectedImageFileUri : Uri? = null
    private var mProfileImageURL : String = ""
    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_profile_content)
        setupActionBar()


        val my_profile_user_image = findViewById<CircleImageView>(R.id.my_profile_user_image)

        my_profile_user_image.setOnClickListener{
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
            {
                showImageChooser()
        } else{
//            ActivityCompat.requestPermissions(this,
//            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
//                Constants.READ_STORAGE_PERMISSION_CODE)
                showImageChooser()
        }
        }

        val my_profile_update_btn = findViewById<Button>(R.id.my_profile_update_btn)

        my_profile_update_btn.setOnClickListener {
            if(mSelectedImageFileUri != null){
                uploadUserImage()
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                updateUserProfileData()
            }
        }

        FirestoreClass().loadUserData(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showImageChooser()
            }
        }else{
            Toast.makeText(this, "Добавьте разрешение на использование хранилища. " +
                    "Вы можете сделать это в настройках", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null){
            mSelectedImageFileUri = data.data
            try{
                val my_profile_user_image = findViewById<CircleImageView>(R.id.my_profile_user_image)

                Glide.with(this)
                    .load(mSelectedImageFileUri)
                    .fitCenter()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(my_profile_user_image)
            }catch (e : IOException){
                e.printStackTrace()
            }

        }
    }

    private fun showImageChooser(){
        var galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, Constants.PICK_IMAGE_REQUEST_CODE)
    }


    private fun setupActionBar(){
        val toolbar_my_profile_activity = findViewById<Toolbar>(R.id.toolbar_my_profile_activity)

        setSupportActionBar(toolbar_my_profile_activity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
            actionBar.title = resources.getString(R.string.my_profile_title)
        }

        toolbar_my_profile_activity.setNavigationOnClickListener { onBackPressed() }

    }

    fun setUserDataInUI(user : User){

        mUserDetails = user

        val my_profile_user_image = findViewById<CircleImageView>(R.id.my_profile_user_image)

        //set user image
        Glide.with(this)
            .load(user.image)
            .fitCenter()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(my_profile_user_image)
        //set user details

        val my_profile_et_name = findViewById<EditText>(R.id.my_profile_et_name)
        val my_profile_et_email = findViewById<EditText>(R.id.my_profile_et_email)
        val my_profile_et_mobile = findViewById<EditText>(R.id.my_profile_et_mobile)

        my_profile_et_name.setText(user.name)
        my_profile_et_email.setText(user.email)

        if(user.mobile != 0L){
            my_profile_et_mobile.setText(user.mobile.toString())
        }
    }

    private fun updateUserProfileData(){
        var userHashMap = HashMap<String, Any>()
        var anyChangesMade : Boolean = false

        if(mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image){
            userHashMap[Constants.IMAGE] = mProfileImageURL
            anyChangesMade = true
        }
        val my_profile_et_name = findViewById<EditText>(R.id.my_profile_et_name)
        val my_profile_et_mobile = findViewById<EditText>(R.id.my_profile_et_mobile)

        if(my_profile_et_name.text.toString() != mUserDetails.name){
            userHashMap[Constants.NAME] = my_profile_et_name.text.toString()
            anyChangesMade = true
        }

        if(my_profile_et_mobile.text.toString() != mUserDetails.mobile.toString()){
            userHashMap[Constants.MOBILE] = my_profile_et_mobile.text.toString().toLong()
            anyChangesMade = true
        }

        if (anyChangesMade){
            FirestoreClass().updateUserProfileData(this, userHashMap)
        }
        else{
            Toast.makeText(this,"Вы не внесли никаких изменений!",
                Toast.LENGTH_SHORT).show()
            hideProgressDialog()
        }

    }

    private fun uploadUserImage(){
        showProgressDialog(resources.getString(R.string.please_wait))

        if (mSelectedImageFileUri != null){
            val sRef : StorageReference = FirebaseStorage.getInstance().reference
                .child("USER_IMAGE" + System.currentTimeMillis() +
                "." + getFileExtension(mSelectedImageFileUri))

            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener {
                taskSnapshot ->
                Log.e("Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    uri ->
                    Log.i("Downloadable Image URL", uri.toString())
                    mProfileImageURL = uri.toString()

                    updateUserProfileData()
                }
            }.addOnFailureListener{
                exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                hideProgressDialog()
            }
        }

    }

    private fun getFileExtension(uri: Uri?): String?{
        return MimeTypeMap.getSingleton().
            getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    fun profileUpdateSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

}