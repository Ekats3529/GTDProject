package com.example.gtdproject.activities


import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.gtdproject.R

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        getSupportActionBar()?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val btn_sing_up = findViewById<Button>(R.id.btn_sign_up_intro)

        btn_sing_up.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        val btn_sing_in = findViewById<Button>(R.id.btn_sign_in_intro)

        btn_sing_in.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}