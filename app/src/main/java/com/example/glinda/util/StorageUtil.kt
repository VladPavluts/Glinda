package com.example.glinda.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.security.cert.CertPath
import java.util.*

object StorageUtil {
    private val storageInstance: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    private val storageCurrRef:StorageReference
    get() = storageInstance.reference.child(FirebaseAuth.getInstance().currentUser?.uid
        ?: throw NullPointerException("UID is null"))

    fun uploadProfilePhoto(imageBytes: ByteArray, onSuccess: (imagePath: String)-> Unit){
        val ref = storageCurrRef.child("/profilePicture"+UUID.nameUUIDFromBytes(imageBytes))
        ref.putBytes(imageBytes).addOnSuccessListener {
            onSuccess(ref.path)
        }
    }
    fun getCurrentRef(path: String)= storageInstance.getReference(path)


}