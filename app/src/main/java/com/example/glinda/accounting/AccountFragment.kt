package com.example.glinda.accounting

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

import com.example.glinda.R
import com.example.glinda.glide.GlideApp
import com.example.glinda.util.FirestoreUtil
import com.example.glinda.util.StorageUtil
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.account_fragment.*
import kotlinx.android.synthetic.main.account_fragment.view.*
import java.io.ByteArrayOutputStream

class AccountFragment : Fragment() {

    private val PICK_IMAGE_REQUEST=2222
    private lateinit var selectedImageBytes:ByteArray
    private var isImageChanged=false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.account_fragment,container,false)

        view.apply {
            profile_photo.setOnClickListener {
                val intent= Intent().apply {
                    type="image/*"
                    action=Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg","image/png"))
                }
                startActivityForResult(Intent.createChooser(intent,"Select image"),PICK_IMAGE_REQUEST)
            }
        }
        val accSaveButton=view.findViewById<Button>(R.id.accSaveButton)
        accSaveButton.setOnClickListener {
            if(::selectedImageBytes.isInitialized){
                StorageUtil.uploadProfilePhoto(selectedImageBytes){imagePath ->  
                    FirestoreUtil.updateCurrentUser(nameEditText.text.toString(),bioEditText.text.toString(),imagePath)
                }
            } else{
                FirestoreUtil.updateCurrentUser(nameEditText.text.toString(),bioEditText.text.toString(),null)
            }
        }
        val accLogOutButton=view.findViewById<Button>(R.id.accLogOutButton)

        accLogOutButton.setOnClickListener {
            AuthUI.getInstance()
                .signOut(this@AccountFragment.context!!)
                .addOnCompleteListener {
                    findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToLoginFragment())
                }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.data!=null){
            val selectedImagePath=data.data
            val selectedImageBmp=MediaStore.Images.Media.getBitmap(activity?.contentResolver,selectedImagePath)

            val outputStream=ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG,90,outputStream)

            selectedImageBytes=outputStream.toByteArray()

            GlideApp.with(this)
                .load(selectedImageBytes)
                .into(profile_photo)

            isImageChanged=true
        }

    }

    override fun onStart() {
        super.onStart()
        FirestoreUtil.getCurrentUser { user ->
            if(this@AccountFragment.isVisible){
                nameEditText.setText(user.name)
                bioEditText.setText(user.bio)
                if(!isImageChanged && user.profilePicturePath!=null){
                    GlideApp.with(this)
                        .load(StorageUtil.getCurrentRef(user.profilePicturePath))
                        .placeholder(R.drawable.ic_account_circle_biriz_24dp)
                        .into(profile_photo)
                }
            }

        }
    }


}
