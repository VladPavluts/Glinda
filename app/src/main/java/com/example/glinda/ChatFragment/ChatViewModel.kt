package com.example.glinda.ChatFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glinda.model.User
import com.example.glinda.util.FirestoreUtil
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    val usersLiveData: MutableLiveData<List<User>> = MutableLiveData()
    val users: LiveData<List<User>> = usersLiveData

    fun getFromDb(){

        viewModelScope.launch {
           usersLiveData.value=FirestoreUtil.getListOfUsers()
        }
    }
}
