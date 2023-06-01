package com.example.gtdproject.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.gtdproject.R
import com.example.gtdproject.firebase.FirestoreClass
import com.example.gtdproject.models.Task

class CreateTaskActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)

        setActionBar()
    }


    private fun setActionBar() {

        val toolbar_create_task_activity = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_create_task_activity)

        setSupportActionBar(toolbar_create_task_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
            actionBar.title = "Создать заметку"
        }
        toolbar_create_task_activity.setNavigationOnClickListener { onBackPressed() }

        val create_task_btn = findViewById<Button>(R.id.create_task_btn)

        create_task_btn.setOnClickListener {
            showProgressDialog(resources.getString(R.string.please_wait))
            createTask()
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

    private fun createTask() {
        val create_task_title = findViewById<EditText>(R.id.create_task_title)
        val create_task_entry = findViewById<EditText>(R.id.create_task_entry)

        hideProgressDialog()

        intent = Intent(this, QuizQuestionsActivity::class.java)
            .putExtra("id", getCurrentUserID() + System.currentTimeMillis().toString())
            .putExtra("title", create_task_title.text.toString())
            .putExtra("entry", create_task_entry.text.toString())
            .putExtra("UserID", getCurrentUserID())

        startActivity(intent)
        finish()

    }
}