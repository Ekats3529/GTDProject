package com.example.gtdproject.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.gtdproject.activities.*
import com.example.gtdproject.models.User
import com.example.gtdproject.models.Task
import com.example.gtdproject.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()


    fun registerUser(activity: SignUpActivity, userInfo: User){
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }.addOnFailureListener{
                e->
                Log.e(activity.javaClass.simpleName, "Ошибка")
            }
    }

    fun createTask(activity: QuizQuestionsActivity, taskInfo: Task){

        mFirestore.collection(Constants.TASKS)
            .document()
            .set(taskInfo, SetOptions.merge())
            .addOnSuccessListener {
                //Toast.makeText(activity,"Task created successfully", Toast.LENGTH_SHORT).show()
                //activity.taskCreatedSuccessfully() //show toast to the user
            }.addOnFailureListener{
                //activity.hideProgressDialog()
            }
    }

    fun updateUserProfileData(activity: MyProfileActivity,
                              userHashMap: HashMap<String, Any>){
            mFirestore.collection(Constants.USERS)
                .document(getCurrentUserID())
                .update(userHashMap)
                .addOnSuccessListener {
                    Log.i(activity.javaClass.simpleName, "Profile Data updated")
                    Toast.makeText(activity,"Данные вашего профиля успешно изменены!",
                        Toast.LENGTH_SHORT).show()
                    activity.profileUpdateSuccess()
                }.addOnFailureListener{
                    e ->
                    activity.hideProgressDialog()
                    Toast.makeText(activity,"Ошибка при обновлении данных профиля!",
                        Toast.LENGTH_SHORT).show()
                }
    }


    fun loadUserData(activity: Activity){
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener {document ->
                val loggedInUser = document.toObject(User::class.java)!!

                when(activity){
                    is SignInActivity -> {activity.signInSuccess(loggedInUser)}
                    is MainActivity -> { activity.updateNavigationUserDetails(loggedInUser) }
                    is MyProfileActivity ->{activity.setUserDataInUI(loggedInUser)}
                }

            }.addOnFailureListener{
                    e->

                when(activity){
                    is SignInActivity -> {activity.hideProgressDialog()}
                    is MainActivity -> { activity.hideProgressDialog() }
                    is MyProfileActivity -> { activity.hideProgressDialog() }
                }
                Log.e("SignInUser", "Ошибка")
            }
    }

    fun getTasksList(activity: ShowTasksActivity, status: String){

        mFirestore.collection(Constants.TASKS)
            .whereEqualTo(Constants.USERID, getCurrentUserID())
            .whereEqualTo(Constants.STATUS, status)
            .get()
            .addOnSuccessListener {
                document ->
                Log.e("GetTaskList", document.toString())
                val tasksList : ArrayList<HashMap<String, Any>> = ArrayList()
                for (i in  document.documents){
                    val task = i.toObject(Task::class.java)!!
                    task.id = i.id
                    val taskhm: HashMap<String, Any> = HashMap()
                    taskhm["title"] = task.title
                    taskhm["entry"] = task.entry
                    tasksList.add(taskhm)
                }
                activity.populateTasksToUI(tasksList)
            }.addOnFailureListener {

                //activity.hideProgressDialog()
            }
    }


    fun getCurrentUserID(): String{

        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }




}