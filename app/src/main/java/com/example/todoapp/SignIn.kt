package com.example.todoapp

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todoapp.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignIn : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
    }

    private fun init (view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

    private fun registerEvents () {
        binding.AuthtextView.setOnClickListener {
            navControl.navigate(R.id.action_signIn_to_signUp)
        }
        binding.NxtBtn.setOnClickListener {
            val email = binding.Email.text.toString().trim()
            val pass = binding.Password.text.toString().trim()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                binding.probar.visibility = View.VISIBLE
                auth.signInWithEmailAndPassword(email , pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context , "Login Successfully" , Toast.LENGTH_SHORT).show()
                        navControl.navigate(R.id.action_signIn_to_home)
                    } else {
                        Toast.makeText(context , "Incorrect informations" , Toast.LENGTH_SHORT).show()
                    }
                    binding.probar.visibility = View.GONE
                }
            }
            else {
                Toast.makeText(context , "empty not allowed" , Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
