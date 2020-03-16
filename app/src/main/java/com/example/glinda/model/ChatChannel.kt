package com.example.glinda.model

data class ChatChannel(val usersId:MutableList<String>){
    constructor():this(mutableListOf())
}