package com.example.todoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todoapp.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUp : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
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
            navControl.navigate(R.id.action_signUp_to_signIn)
        }

        binding.NxtBtn.setOnClickListener {
            val Fname = binding.FirstName.text.toString().trim()
            val Lname = binding.LastName.text.toString().trim()
            val email = binding.Email.text.toString().trim()
            val pass = binding.Password.text.toString().trim()
            val confPass = binding.ConfPass.text.toString().trim()

            if (Fname.isNotEmpty() && Lname.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confPass.isNotEmpty()) {
                if (pass == confPass) {
                    binding.probar.visibility = View.VISIBLE
                    auth.createUserWithEmailAndPassword(email , pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(context , "Registration Successfully Completed" , Toast.LENGTH_SHORT).show()
                            navControl.navigate(R.id.action_signUp_to_home)
                        } else {
                            Toast.makeText(context , it.exception?.message , Toast.LENGTH_SHORT).show()
                        }
                        binding.probar.visibility = View.GONE
                    }
                } else {
                    Toast.makeText(context , "password confirmation incorrect" , Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
