package com.example.chatwithfirestore.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatwithfirestore.data.models.UserModel
import com.example.chatwithfirestore.databinding.UserItemBinding
import com.example.chatwithfirestore.utills.OnClick

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    private var users:ArrayList<UserModel> = ArrayList()
    private lateinit var onClickUser: OnClick

    fun onClick(onClick: OnClick){
        onClickUser = onClick
    }

    fun addUsers(user:ArrayList<UserModel>){
       this.users = user
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(UserItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class ViewHolder(private val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(s: UserModel)= with(binding){
            login.text = s.login
            root.setOnClickListener {
                onClickUser.onClick(s)
            }
            name.text = s.name
        }
    }
}