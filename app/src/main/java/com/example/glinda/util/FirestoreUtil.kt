package com.example.glinda.util

import com.example.glinda.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirestoreUtil {
    private val firestoreinstance:FirebaseFirestore by lazy {FirebaseFirestore.getInstance()}
    private val users= firestoreinstance.collection("users")

    private  val currentUserDoc:DocumentReference
        get() = users.document(FirebaseAuth.getInstance().currentUser?.uid
            ?: throw NullPointerException("UID is null"))

    fun initCurrentUserFirstTime(onComplete:() -> Unit){
        currentUserDoc.get().addOnSuccessListener{documentSnapshot ->
            if(!documentSnapshot.exists()){
                val newUser=User(FirebaseAuth.getInstance().currentUser?.displayName ?: "","",null)
                currentUserDoc.set(newUser).addOnSuccessListener{
                    onComplete()
                }
            } else{
                onComplete()
            }
        }
    }

    fun updateCurrentUser(name: String="",bio: String="",profilePicturePath: String? = null){
        val userMap= mutableMapOf<String,Any>()
        if(name.isNotBlank())
            userMap["name"]=name
        if(bio.isNotBlank())
            userMap["bio"]=bio
        if (profilePicturePath!=null)
            userMap["profilePicturePath"]=profilePicturePath
        currentUserDoc.update(userMap)
    }

    fun getCurrentUser(onComplete: (User) -> Unit){
        currentUserDoc.get().addOnSuccessListener{onComplete(it.toObject(User::class.java)!!)}
    }
    private suspend fun getListData():List<DocumentSnapshot>{
        val snapshot= users.get().await()
        return snapshot.documents

    }
    suspend fun getListOfUsers():MutableList<User> {
        val users_list = mutableListOf<User>()
        val listDocSn:List<DocumentSnapshot> = getListData()
        for (document in listDocSn) {
            if (document.exists()){
                users_list.add(document.toObject(User::class.java)!!)
            }
        }
        return  users_list
    }
}