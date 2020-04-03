package com.example.glinda

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

object Repository {
    private val mAuth:FirebaseAuth= FirebaseAuth.getInstance()

    fun signIn(email: String,password: String,completionListener:(isSuccessful: Boolean)-> Unit){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
                task -> completionListener(task.isSuccessful)
            if (task.isSuccessful) {
                Log.d(Const.TAG, "${task.result?.user?.uid}")
            }
        }
    }
}