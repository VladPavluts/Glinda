package com.example.glinda.chat_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glinda.model.UserItem
import com.example.glinda.util.FirestoreUtil
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    val usersLiveData: MutableLiveData<List<UserItem>> = MutableLiveData()
    val users: LiveData<List<UserItem>> = usersLiveData

    fun getFromCloudSt(){
        viewModelScope.launch {
           usersLiveData.value=FirestoreUtil.getListOfUsers()
        }
    }
}
