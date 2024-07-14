package com.example.shopu.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mercaditu.di.SupabaseClient
import com.example.mercaditu.repository.AuthenticationRepository
import com.example.mercaditu.repository.implementation.AuthenticationRepositoryImpl
import com.example.shopu.R
import com.example.shopu.viewmodel.AuthState
import com.example.shopu.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class Login : Fragment() {
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email_field = view.findViewById<EditText>(R.id.campo_email)
        val password_field = view.findViewById<EditText>(R.id.campo_contraseña)

        val button_login = view.findViewById<Button>(R.id.boton_login)
        val registerButton = view.findViewById<TextView>(R.id.registrate_enlace)
        val button_google = view.findViewById<ImageView>(R.id.boton_google)
        val resetPasswordButton = view.findViewById<TextView>(R.id.olvidaste_enlace)

        val supabaseClient = SupabaseClient()
        val auth = supabaseClient.getAuth()
        val authenticationRepository: AuthenticationRepository = AuthenticationRepositoryImpl(auth)
        authViewModel = AuthViewModel(authenticationRepository)

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)


        lifecycleScope.launch {
            authViewModel.email.collect { email ->
                if (email_field.text.toString() != email) {
                    email_field.setText(email)
                }
            }
        }

        lifecycleScope.launch {
            authViewModel.password.collect { password ->
                if (password_field.text.toString() != password) {
                    password_field.setText(password)
                }
            }
        }

        email_field.addTextChangedListener {
            authViewModel.onEmailChange(it.toString())
        }

        password_field.addTextChangedListener {
            authViewModel.onPasswordChange(it.toString())
        }

        button_login.setOnClickListener {
            if (email_field.text.toString()!= "" && password_field.text.toString() != ""){
                authViewModel.onSignIn()
                lifecycleScope.launch {
                    authViewModel.authState.collectLatest { state ->
                        if (state is AuthState.Success) {
                            builder
                                .setMessage("Ingreso exitoso")
                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                            findNavController().navigate(R.id.action_login_to_home2)
                        }
                    }
                }
            }else{
                builder
                    .setMessage("Los campos no pueden estar vacios")
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }

        button_google.setOnClickListener {
            authViewModel.onGoogleSignIn()
            lifecycleScope.launch {
                authViewModel.authState.collectLatest { state ->
                    if (state is AuthState.Success) {
                        builder
                            .setMessage("Ingreso exitoso")
                        val dialog: AlertDialog = builder.create()
                        dialog.show()
                        findNavController().navigate(R.id.action_login_to_home2)
                    }
                }
            }
        }

        registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }

        resetPasswordButton.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_forgetPassword)
        }
    }
}