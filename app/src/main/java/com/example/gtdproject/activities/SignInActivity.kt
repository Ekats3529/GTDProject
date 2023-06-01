package com.example.gtdproject.activities


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.gtdproject.R
import com.example.gtdproject.firebase.FirestoreClass
import com.example.gtdproject.models.User
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        getSupportActionBar()?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val btn_sing_in = findViewById<Button>(R.id.btn_sign_in)

        btn_sing_in.setOnClickListener {
            signInRegisteredUser()
        }

        setupActionBar()

    }

    fun signInSuccess (user: User){
        hideProgressDialog()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setupActionBar(){
        val toolbar_sign_in = findViewById<Toolbar>(R.id.toolbar_sign_in)

        setSupportActionBar(toolbar_sign_in)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(com.google.android.material.R.drawable.ic_arrow_back_black_24)
        }

        toolbar_sign_in.setNavigationOnClickListener { onBackPressed() }
    }

    fun signInRegisteredUser(){
        val et_email = findViewById<EditText>(R.id.et_email_in)
        val email: String = et_email.text.toString().trim{it <= ' '}

        val et_password = findViewById<EditText>(R.id.et_password_in)
        val password: String = et_password.text.toString().trim{it <= ' '}

        if (validateForm(email, password)){
            showProgressDialog(resources.getString(R.string.please_wait))

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        FirestoreClass().loadUserData(this)

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Sign in", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Ошибка авторизации",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }


    }

    private fun validateForm(email: String, password: String) :Boolean {

        return when{
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