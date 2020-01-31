package com.example.glinda.login

import android.widget.EditText
import androidx.databinding.BindingAdapter

@BindingAdapter("emailFocus")
fun EditText.setEmailFocusListener(item: LoginViewModel) {
    setOnFocusChangeListener { _, focused ->
        if(!focused){
            item.checkEmail()
        }
    }
}

@BindingAdapter("passwordFocus")
fun EditText.setPasswordFocusListener(item: LoginViewModel) {
    setOnFocusChangeListener { _, focused ->
        if(!focused){
            item.checkPassword()
        }
    }
}