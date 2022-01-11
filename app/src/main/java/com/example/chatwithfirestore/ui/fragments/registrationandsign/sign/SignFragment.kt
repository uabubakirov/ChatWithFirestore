package com.example.chatwithfirestore.ui.fragments.registrationandsign.sign


import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.chatwithfirestore.R
import com.example.chatwithfirestore.databinding.FragmentSignBinding
import com.example.chatwithfirestore.utills.Constants
import com.example.chatwithfirestore.utills.PreferenceManager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


class SignFragment : Fragment() {

    private lateinit var bindng: FragmentSignBinding
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindng = FragmentSignBinding.inflate(inflater,container,false)
        return bindng.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
        setupListeners()

    }

    private fun setupListeners() {
        bindng.txtRegistration.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.registrationFragment)
        }
        bindng.btnSign.setOnClickListener {
            if (signDetails()){
                signIn()
            }
        }
    }

    private fun signIn() {
        val db = FirebaseFirestore.getInstance()
        db.collection(Constants.KEY_COLLECTION_USER)
            .whereEqualTo(Constants.KEY_LOGIN,bindng.etLogin.text.toString())
            .whereEqualTo(Constants.KEY_PASSWORD,bindng.etPassword.text.toString())
            .get()
            .addOnCompleteListener{
                if (it.isSuccessful && it.result != null && it.result!!.documents.size > 0 ){
                    var documentSnapshot: DocumentSnapshot = it.result!!.documents.get(0)
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true)
                    preferenceManager.putString(Constants.KEY_USER_ID,documentSnapshot.id)
                    preferenceManager.putString(Constants.KEY_NAME,
                        documentSnapshot.getString(Constants.KEY_NAME)!!)
                    findNavController().navigate(R.id.mainFragment)
                }else{
                    showToast("Unable to sign")
                }
            }
    }

    private fun showToast(message: String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }

    private fun signDetails():Boolean{
        if (bindng.etLogin.text.toString().trim().isEmpty()){
            showToast("Enter Login")
            return false
        }else if (bindng.etPassword.text.toString().trim().isEmpty()){
            showToast("Enter Password")
            return false
        }else{
            return true
        }
    }


}