package com.example.chatwithfirestore.data.models

import java.io.Serializable
import java.util.logging.LogManager

 class UserModel : Serializable{
     lateinit var name: String
     lateinit var login: String
     lateinit var id: String
 }

