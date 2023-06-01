package com.example.gtdproject.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gtdproject.R
import com.example.gtdproject.RecyclerAdapter
import com.example.gtdproject.firebase.FirestoreClass
import com.example.gtdproject.models.Task
import com.example.gtdproject.models.User
import com.example.gtdproject.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class ShowTasksActivity : BaseActivity() {

    private var titlesList = mutableListOf<String>()
    private var entriesList = mutableListOf<String>()
    lateinit var status : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_tasks)

        status = intent.getStringExtra("type").toString()

        setupActionBar()

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getTasksList(this, Constants.getEnglishName(status))


    }


    private fun postToList(tasksList : ArrayList<HashMap<String, Any>>){
        for (item in tasksList){
            titlesList.add(item["title"] as String)
            entriesList.add(item["entry"] as String)
        }
    }

    private fun setupActionBar(){
        val toolbar_show_tasks = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_show_tasks)

        setSupportActionBar(toolbar_show_tasks)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)

            actionBar.title = status
        }

        toolbar_show_tasks.setNavigationOnClickListener { onBackPressed() }

    }

    fun populateTasksToUI(tasksList : ArrayList<HashMap<String, Any>>){
        hideProgressDialog()

        if(tasksList.size > 0 ){

            postToList(tasksList)

            val rv_recyclerView = findViewById<RecyclerView>(R.id.rv_recyclerView)

            rv_recyclerView.layoutManager = LinearLayoutManager(this)
            rv_recyclerView.adapter = RecyclerAdapter(titlesList, entriesList)

        }
    }






}

