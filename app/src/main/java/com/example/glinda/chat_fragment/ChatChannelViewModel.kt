package com.example.glinda.chat_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glinda.model.TextMessage
import com.example.glinda.util.FirestoreUtil
import kotlinx.coroutines.launch

class ChatChannelViewModel : ViewModel(){
    val messagesLiveData: MutableLiveData<List<TextMessage>> = MutableLiveData()
    val messages: LiveData<List<TextMessage>> = messagesLiveData

    val _channelId:MutableLiveData<String> = MutableLiveData()
    val channelId:LiveData<String> = _channelId

    fun getFromMessagCloud(user_id:String){
        viewModelScope.launch {
            val channelId1= FirestoreUtil.getOrCreateChatChannel(user_id)
            _channelId.value=channelId1
            messagesLiveData.value = FirestoreUtil.getListOfMessage(channelId1)
        }
    }

    fun sendMessage(messageToSend:TextMessage){
        FirestoreUtil.sendMessage(messageToSend, channelId.value!!)
    }
}