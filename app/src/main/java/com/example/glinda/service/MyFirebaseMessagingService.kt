package com.example.glinda.service

import android.util.Log
import com.example.glinda.util.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(newRegistrationToken: String) {
        super.onNewToken(newRegistrationToken)
        if(FirebaseAuth.getInstance().currentUser!=null)
            addTokenToFirestore(newRegistrationToken)
    }

    companion object {
        fun addTokenToFirestore(newRegistrationToken: String?) {
            if (newRegistrationToken == null)
                throw NullPointerException("FCM token is null.")

            FirestoreUtil.getFCMRegistrationTokens { tokens ->
                if (tokens.contains(newRegistrationToken))
                    return@getFCMRegistrationTokens

                tokens.add(newRegistrationToken)
                FirestoreUtil.setFCMRegistrationTokens(tokens)
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.notification != null){
            Log.d("FCM",remoteMessage.data.toString())
        }
    }
}