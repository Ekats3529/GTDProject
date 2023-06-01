package com.example.gtdproject.utils

import com.example.gtdproject.R
import com.example.gtdproject.models.Question

object Constants {
    const val USERS: String = "users"
    const val TASKS : String = "tasks"

    const val READ_STORAGE_PERMISSION_CODE = 1
    const val PICK_IMAGE_REQUEST_CODE = 2

    const val IMAGE : String = "image"
    const val NAME : String = "name"
    const val MOBILE : String = "mobile"

    const val USERID : String = "userID"
    const val STATUS : String = "status"
    const val TITLE : String = "title"
    const val ENTRY : String = "entry"

    const val INBOX : String = "Входящие материалы"
    const val NOTES : String = "Заметки"
    const val WAITING : String = "Ожидание"
    const val SOMEDAY : String = "Когда-нибудь потом"
    const val PROJECTS : String = "Проекты"
    const val CUR_ACTIONS : String = "Текущие действия"
    const val DONE : String = "Сделано"
    const val TRASH : String = "Мусор"

    fun getEnglishName(name: String): String{
        if(name == "Входящие материалы"){ return "inbox" }
        if (name == "Заметки"){ return "notes" }
        if(name == "Ожидание"){ return "waiting" }
        if (name == "Когда-нибудь потом"){ return "someday" }
        if(name == "Проекты"){ return "projects" }
        if (name == "Текущие действия"){ return "cur_actions" }
        if(name == "Сделано"){ return "done" }
        if (name == "Мусор"){ return "trash" }
        else{
            return ""
        }
    }



    const val MY_PROFILE_REQUEST_CODE : Int = 11

    fun getQuestions(): ArrayList<Question>{
        val questionsList = ArrayList<Question>()

        val que1 = Question(1,
            "Вы хотите отсортировать эту заметку?",
            R.drawable.ic_question1,
            "Входящие материалы")

        val que2 = Question(2,
            "С этим надо что-то делать?",
            R.drawable.ic_question2,
        "Заметки")

        val que3 = Question(3,
            "Мне?",
            R.drawable.ic_question3,
            "Ожидание")

        val que4 = Question(4,
            "Сейчас?",
            R.drawable.ic_question4,
            "Когда-нибудь потом")

        val que5 = Question(5,
            "Задача одношаговая?",
            R.drawable.ic_question5,
            "Проекты")

        val que6 = Question(6,
            "Можно сделать за 5 минут?",
            R.drawable.ic_question6,
            "Текущие действия")

        questionsList.add(que1)
        questionsList.add(que2)
        questionsList.add(que3)
        questionsList.add(que4)
        questionsList.add(que5)
        questionsList.add(que6)

        return questionsList

    }

}