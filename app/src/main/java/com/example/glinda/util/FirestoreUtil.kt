package com.example.glinda.util

import com.example.glinda.model.ChatChannel
import com.example.glinda.model.TextMessage
import com.example.glinda.model.User
import com.example.glinda.model.UserItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirestoreUtil {
    private val firestoreinstance:FirebaseFirestore by lazy {FirebaseFirestore.getInstance()}
    private val users= firestoreinstance.collection("users")
    private val chatChannelsCurrentRef= firestoreinstance.collection("chatChannels")

    private  val currentUserDocRef:DocumentReference
        get() = users.document(FirebaseAuth.getInstance().currentUser?.uid
            ?: throw NullPointerException("UID is null"))


    fun initCurrentUserFirstTime(onComplete:() -> Unit){
        currentUserDocRef.get().addOnSuccessListener{documentSnapshot ->
            if(!documentSnapshot.exists()){
                val newUser=User(FirebaseAuth.getInstance().currentUser?.displayName ?: "","",null,
                    mutableListOf())
                currentUserDocRef.set(newUser).addOnSuccessListener{
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
        currentUserDocRef.update(userMap)
    }

    fun getCurrentUser(onComplete: (User) -> Unit){
        currentUserDocRef.get().addOnSuccessListener{onComplete(it.toObject(User::class.java)!!)}
    }
    private suspend fun getListData():List<DocumentSnapshot>{
        val snapshot= users.get().await()
        return snapshot.documents

    }
    suspend fun getListOfUsers():List<UserItem> {
        val users_list = mutableListOf<UserItem>()
        val listDocSn:List<DocumentSnapshot> = getListData()
        for (document in listDocSn) {
            if (document.exists()){
                document.id
                users_list.add(UserItem(document.toObject(User::class.java)!!,document.id))
            }
        }
        return  users_list
    }

    private suspend fun getMessageData(otherUserID: String):DocumentSnapshot{
        val snapshot= currentUserDocRef.collection("engagedChatChannels")
            .document(otherUserID)
            .get()
            .await()
        return snapshot

    }
    suspend fun getOrCreateChatChannel(otherUserID:String):String{

        val document= getMessageData(otherUserID)
        if(document.exists()){
            val chanStr:String=document["channelId"] as String
            return chanStr
        }


        val currentUserId=FirebaseAuth.getInstance().currentUser!!.uid

        val newChannel = chatChannelsCurrentRef.document()
        newChannel.set(ChatChannel(mutableListOf(currentUserId,otherUserID)))
        currentUserDocRef.collection("engagedChatChannels")
            .document(otherUserID)
            .set(mapOf("channelId" to newChannel.id))

        users.document(otherUserID)
            .collection("engagedChatChannels")
            .document(currentUserId)
            .set(mapOf("channelId" to newChannel.id))

        return newChannel.id
    }
    private suspend fun getListMessagesData(channelId: String):List<DocumentSnapshot>{
        val snapshot=chatChannelsCurrentRef.document(channelId).collection("messages")
            .orderBy("time")
            .get()
            .await()
        return  snapshot.documents
    }
    suspend fun getListOfMessage(channelId: String):List<TextMessage>{
        val listDocSn:List<DocumentSnapshot> = getListMessagesData(channelId)
        val messages_list = mutableListOf<TextMessage>()
        for (document in listDocSn) {
            if (document.exists()){
                messages_list.add(document.toObject(TextMessage::class.java)!!)
            }
        }
        return messages_list

    }
    fun sendMessage(message: TextMessage, channelId: String) {
        chatChannelsCurrentRef.document(channelId)
            .collection("messages")
            .add(message)
    }

    fun getFCMRegistrationTokens(onComplete: (tokens: MutableList<String>) -> Unit) {
        currentUserDocRef.get().addOnSuccessListener {
            val user = it.toObject(User::class.java)!!
            onComplete(user.registrationTokens)
        }
    }

    fun setFCMRegistrationTokens(registrationTokens: MutableList<String>) {
        currentUserDocRef.update(mapOf("registrationTokens" to registrationTokens))
    }
}