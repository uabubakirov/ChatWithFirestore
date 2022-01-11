package com.example.chatwithfirestore.utills

import com.example.chatwithfirestore.data.models.UserModel

interface OnClick {
    fun onClick(user: UserModel)
}