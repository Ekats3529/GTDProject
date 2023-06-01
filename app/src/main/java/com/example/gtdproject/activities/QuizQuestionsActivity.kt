package com.example.gtdproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.gtdproject.R
import com.example.gtdproject.firebase.FirestoreClass
import com.example.gtdproject.models.Question
import com.example.gtdproject.models.Task
import com.example.gtdproject.utils.Constants

class QuizQuestionsActivity : BaseActivity() {
    var mCurrentPosition: Int = 1
    val mQuestionsList = Constants.getQuestions()
    var question : Question? = mQuestionsList[mCurrentPosition - 1]
    lateinit var id : String
    lateinit var title : String
    lateinit var userID : String
    lateinit var entry : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        id = intent.getStringExtra("id").toString()
        title = intent.getStringExtra("title").toString()
        userID = intent.getStringExtra("UserID").toString()
        entry = intent.getStringExtra("entry").toString()

        showNextQuestion()

        val btn_true = findViewById<Button>(R.id.btn_true)

        btn_true.setOnClickListener {
            if(mCurrentPosition >= mQuestionsList.size){
                Toast.makeText(this,
                    "Делайте это сейчас!!!",
                    Toast.LENGTH_SHORT).show()
                createTask("Сделано")
                finish()
            }
            else{
                mCurrentPosition++
                showNextQuestion()
            }
        }

        val btn_false = findViewById<Button>(R.id.btn_false)

        btn_false.setOnClickListener {
            showProgressDialog(resources.getString(R.string.please_wait))
            createTask(question!!.status)
            hideProgressDialog()

            Toast.makeText(this,
                "Вы добавили заметку в ${question!!.status}",
                Toast.LENGTH_SHORT).show()

            finish()
        }
    }

    private fun showNextQuestion(){

        question = mQuestionsList[mCurrentPosition - 1]

        val iv_image = findViewById<ImageView>(R.id.iv_image)
        val tv_question = findViewById<TextView>(R.id.tv_question)

        tv_question.text = question!!.question
        iv_image.setImageResource(question!!.image)
    }


    private fun createTask(status : String){

        val task = Task(
            id,
            userID,
            Constants.getEnglishName(status),
            title,
            entry
        )

        FirestoreClass().createTask(this, task)
        finish()
    }

}