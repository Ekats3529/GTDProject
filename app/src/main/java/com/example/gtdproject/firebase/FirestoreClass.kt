package com.example.gtdproject.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.gtdproject.activities.MainActivity
import com.example.gtdproject.activities.MyProfileActivity
import com.example.gtdproject.activities.SignInActivity
import com.example.gtdproject.activities.SignUpActivity
import com.example.gtdproject.models.User
import com.example.gtdproject.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

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

    fun getCurrentUserID(): String{

        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }
}