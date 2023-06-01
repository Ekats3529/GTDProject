package com.example.gtdproject.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.gtdproject.R
import com.example.gtdproject.firebase.FirestoreClass
import com.example.gtdproject.models.User
import com.example.gtdproject.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()

        val nav_view = findViewById<NavigationView>(R.id.nav_view)

        nav_view.setNavigationItemSelectedListener(this)

        FirestoreClass().loadUserData(this)
    }

    private fun setupActionBar(){
        val toolbar_main_act = findViewById<Toolbar>(R.id.toolbar_main_activity)

        setSupportActionBar(toolbar_main_act)

        toolbar_main_act.setNavigationIcon(R.drawable.ic_action_navigation_menu)

        toolbar_main_act.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer(){
        val drawer_layout = findViewById<DrawerLayout>(R.id.drawer_layout)

        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        else{
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        val drawer_layout = findViewById<DrawerLayout>(R.id.drawer_layout)

        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            doubleBackToExit()
        }
    }

    fun updateNavigationUserDetails(user : User){
        //hideProgressDialog()

        val nav_user_image = findViewById<CircleImageView>(R.id.nav_user_image)

        // add image
        Glide
            .with(this)
            .load(user.image)
            .fitCenter()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(nav_user_image)
        // add name

        val tv_username = findViewById<TextView>(R.id.tv_username)
        tv_username.text = user.name



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK &&
                requestCode == Constants.MY_PROFILE_REQUEST_CODE){
            FirestoreClass().loadUserData(this)
        }
        else{
            Log.e("Cancelled", "Cancelled")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawer_layout = findViewById<DrawerLayout>(R.id.drawer_layout)

        when(item.itemId){
            R.id.nav_my_profile ->{
                startActivityForResult(Intent(this,
                    MyProfileActivity::class.java),
                    Constants.MY_PROFILE_REQUEST_CODE)

            }
            R.id.nav_sign_out ->{
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }



}