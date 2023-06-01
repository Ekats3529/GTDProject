package com.example.gtdproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.gtdproject.R
import com.example.gtdproject.firebase.FirestoreClass
import com.example.gtdproject.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        getSupportActionBar()?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val btn_sing_up = findViewById<Button>(R.id.btn_sign_up)

        btn_sing_up.setOnClickListener {
            registerUser()
        }

        setupActionBar()
    }

    fun userRegisteredSuccess(){
        Toast.makeText(
            this, "Вы были успешно " +
                    "зарегистрированы",
            Toast.LENGTH_LONG).show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()

    }

    private fun setupActionBar(){
        val toolbar_sign_up = findViewById<Toolbar>(R.id.toolbar_sign_up)

        setSupportActionBar(toolbar_sign_up)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(com.google.android.material.R.drawable.ic_arrow_back_black_24)
        }

        toolbar_sign_up.setNavigationOnClickListener { onBackPressed() }


    }

    private fun registerUser(){
        val et_name = findViewById<EditText>(R.id.et_name)
        val name: String = et_name.text.toString().trim{it <= ' '}

        val et_email = findViewById<EditText>(R.id.et_email)
        val email: String = et_email.text.toString().trim{it <= ' '}

        val et_password = findViewById<EditText>(R.id.et_password)
        val password: String = et_password.text.toString().trim{it <= ' '}

        if(validateForm(name, email, password)){
            showProgressDialog(resources.getString(R.string.please_wait))

            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid, name, registeredEmail)

                        FirestoreClass().registerUser(this, user)
                    } else {
                        Toast.makeText(
                            this,
                            "Ошибка регистрации", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun validateForm(name: String, email: String, password: String) :Boolean {

        return when{
            TextUtils.isEmpty(name)->{
                showErrorSnackBar("Введите имя")
                false
            }
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Введите email")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Введите пароль")
                false
            }
            else -> {true}
        }

    }



}