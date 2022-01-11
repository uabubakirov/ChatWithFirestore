package com.example.chatwithfirestore.ui.fragments.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.chatwithfirestore.R
import com.example.chatwithfirestore.data.models.ChatModel
import com.example.chatwithfirestore.data.models.UserModel
import com.example.chatwithfirestore.databinding.FragmentChatBinding
import com.example.chatwithfirestore.ui.adapter.ChatAdapter
import com.example.chatwithfirestore.utills.Constants
import com.example.chatwithfirestore.utills.PreferenceManager
import com.google.android.gms.common.config.GservicesValue.value
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: ChatAdapter
    private lateinit var receiverUser: UserModel
    private lateinit var preferenceManager: PreferenceManager
    private var chatMessages: ArrayList<ChatModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setupListeners()
        getName()
        listenMessages()
    }

    private fun setupListeners() {
        binding.btnSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        var message: HashMap<String,Any> = HashMap()
        message.put(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID)!!)
        message.put(Constants.KEY_MESSAGE,binding.etMessage.text.toString())
        message.put(Constants.KEY_RECEIVER_ID,receiverUser.id)
        message.put(Constants.KEY_TIMESTAMP,getTime())
        db.collection(Constants.KEY_COLLECTION_CHAT).add(message)
            binding.etMessage.text = null
    }

    private val eventListener = EventListener<QuerySnapshot> { value, error ->
            if (value != null){
                var count: Int = chatMessages.size
                for (document: DocumentChange in value.documentChanges){
                    if (document.type == DocumentChange.Type.ADDED) {
                        var chatMessage = ChatModel(
                            document.document.getString(Constants.KEY_MESSAGE)!!,
                            document.document.getString(Constants.KEY_SENDER_ID)!!,
                            document.document.getString(Constants.KEY_RECEIVER_ID)!!,
                            document.document.getString(Constants.KEY_TIMESTAMP)!!
                        )
                        chatMessages.add(chatMessage)
                        adapter.addText(chatMessages,preferenceManager.getString(Constants.KEY_USER_ID)!!)
                    }}
                if (count==0){
                    adapter.notifyDataSetChanged()
                }else{
                    adapter.notifyItemRangeInserted(chatMessages.size,chatMessages.size)
                    binding.rvChat.smoothScrollToPosition(chatMessages.size -1)
                }
            }
        }
    private  fun listenMessages(){
        db.collection(Constants.KEY_COLLECTION_CHAT)
            .whereEqualTo(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
            .whereEqualTo(Constants.KEY_RECEIVER_ID,receiverUser.id)
            .addSnapshotListener(eventListener)
        db.collection(Constants.KEY_COLLECTION_CHAT)
            .whereEqualTo(Constants.KEY_SENDER_ID,receiverUser.id)
            .whereEqualTo(Constants.KEY_RECEIVER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
            .addSnapshotListener(eventListener)
    }

    private fun getName() {
        receiverUser = arguments?.getSerializable(Constants.KEY_USER) as UserModel
        binding.txtName.text = receiverUser.name
    }

    private fun getTime(): String {
        val sdf = SimpleDateFormat("hh:mm")
        val curentDate = sdf.format(Date())
        return curentDate

    }

    private fun initialize() {
        adapter = ChatAdapter()
        binding.rvChat.adapter = adapter
        preferenceManager = PreferenceManager(requireContext())
        db = FirebaseFirestore.getInstance()

    }


}