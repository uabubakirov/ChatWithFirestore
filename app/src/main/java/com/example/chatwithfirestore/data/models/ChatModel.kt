package com.example.chatwithfirestore.data.models

import android.content.BroadcastReceiver

data class ChatModel (
    val message: String,
    val senderId: String,
    val receiverId: String,
    val dateTime: String
        )