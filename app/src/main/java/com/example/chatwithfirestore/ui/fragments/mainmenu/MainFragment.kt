package com.example.chatwithfirestore.ui.fragments.mainmenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.chatwithfirestore.R
import com.example.chatwithfirestore.data.models.UserModel
import com.example.chatwithfirestore.databinding.FragmentMainBinding
import com.example.chatwithfirestore.ui.adapter.UsersAdapter
import com.example.chatwithfirestore.utills.Constants
import com.example.chatwithfirestore.utills.OnClick
import com.example.chatwithfirestore.utills.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var adapter:UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
        init()
        getUsers()
        onItemClick()
    }

    private fun onItemClick() {
        adapter.onClick(object : OnClick {
            override fun onClick(user: UserModel) {
                var bundle = Bundle()
                bundle.putSerializable(Constants.KEY_USER,user)
                findNavController().navigate(R.id.chatFragment,bundle)
            }
        })
    }

    private fun init() {
        adapter = UsersAdapter()
        binding.rvUsers.adapter = adapter

    }

    private fun getUsers() {
        val db = FirebaseFirestore.getInstance()
        db.collection(Constants.KEY_COLLECTION_USER)
            .get()
            .addOnCompleteListener {
                var currentUserId: String? = preferenceManager.getString(Constants.KEY_USER_ID)
                if (it.isSuccessful && it.result != null){
                    var users: ArrayList<UserModel> = ArrayList()
                    for (documents:QueryDocumentSnapshot in it.result!!){
                        if (currentUserId.equals(documents.id)){
                            continue
                        }
                        var user = UserModel()
                        user.name = documents.getString(Constants.KEY_NAME)!!
                        user.id = documents.id
                        user.login = documents.getString(Constants.KEY_LOGIN)!!
                        users.add(user)
                    }
                    if (users.size > 0){
                        adapter.addUsers(users)
                    }
                }

            }
    }

}