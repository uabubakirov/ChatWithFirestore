package com.example.chatwithfirestore.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatwithfirestore.data.models.ChatModel
import com.example.chatwithfirestore.databinding.LeftMessageBinding
import com.example.chatwithfirestore.databinding.RightMessageBinding

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val send: Int = 1
    private val receive: Int = 2
    private var messageList: ArrayList<ChatModel> = ArrayList()
    private lateinit var senderId: String

    fun addText(list: ArrayList<ChatModel>,senderId:String){
        messageList = list
        this.senderId = senderId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == send) {
            return ViewHolderTwo(
                RightMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return ViewHolderOne(
                LeftMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == send) {
            val holder = holder as ViewHolderTwo
            holder.bind(messageList[position])
        } else {
            val holder = holder as ViewHolderOne
            holder.bind(messageList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].senderId == senderId) {
            send
        } else {
            receive
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }


    inner class ViewHolderOne(private val binding: LeftMessageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(s: ChatModel) {
            binding.message.text = s.message
            binding.date.text = s.dateTime
        }
    }
    inner class ViewHolderTwo(private val binding: RightMessageBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(s: ChatModel){
            binding.message.text = s.message
            binding.date.text = s.dateTime
        }
    }


}