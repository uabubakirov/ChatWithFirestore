package com.example.chatwithfirestore.ui.fragments.registrationandsign.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.chatwithfirestore.R
import com.example.chatwithfirestore.common.base.BaseFragment

import com.example.chatwithfirestore.databinding.FragmentRegistrationBinding
import com.example.chatwithfirestore.utills.Constants
import com.example.chatwithfirestore.utills.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class RegistrationFragment : Fragment(){

    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnRegistration.setOnClickListener {
            if (registrationDetails()){
                signUp()
            }
        }
    }

    private fun signUp() {
        val db = FirebaseFirestore.getInstance()
        val user: HashMap<String,Any> = HashMap()
        user.put(Constants.KEY_NAME,binding.etName.text.toString())
        user.put(Constants.KEY_LOGIN,binding.etLogin.text.toString())
        user.put(Constants.KEY_PASSWORD,binding.etPassword.text.toString())
        db.collection(Constants.KEY_COLLECTION_USER)
            .add(user)
            .addOnSuccessListener {
                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true)
                preferenceManager.putString(Constants.KEY_USER_ID,it.id)
                preferenceManager.putString(Constants.KEY_LOGIN,binding.etLogin.text.toString())
                preferenceManager.putString(Constants.KEY_NAME,binding.etName.text.toString())
                preferenceManager.putString(Constants.KEY_PASSWORD,binding.etPassword.text.toString())
                showToast("Success")
                findNavController().navigate(R.id.signFragment)

            }

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }

    private fun registrationDetails():Boolean {
        if (binding.etLogin.text.toString().trim().isEmpty()){
            showToast("Enter Login")
            return false
        }else if (binding.etName.text.toString().trim().isEmpty()){
            showToast("Enter name")
            return false
        }else if (binding.etPassword.text.toString().trim().isEmpty()){
            showToast("Enter password")
            return false
        }else if (!binding.etPasswordCheck.text.toString().trim().equals(binding.etPassword.text.toString())){
            showToast("Password must be the same")
            return false
        }else{
            return true
        }
    }

    private fun initialize() {

    }


}