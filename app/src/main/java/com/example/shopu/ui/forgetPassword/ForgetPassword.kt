package com.example.shopu.ui.forgetPassword

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shopu.R
import com.example.shopu.repository.AuthenticationRepository
import com.example.shopu.repository.implementation.AuthenticationRepositoryImpl
import com.example.shopu.viewmodel.AuthState
import com.example.shopu.viewmodel.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ForgetPassword : Fragment() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var authRepository: AuthenticationRepository
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forget_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authRepository = AuthenticationRepositoryImpl(auth)
        authViewModel = AuthViewModel(authRepository)

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        var campo_correo_forget_password = view.findViewById<EditText>(R.id.campo_correo_forget_password)
        var boton_recuperar_contraseña = view.findViewById<Button>(R.id.boton_recuperar_contraseña)
        var enlace_recuerdas_contraseña = view.findViewById<TextView>(R.id.texto_recuerdas_contraseña)

        campo_correo_forget_password.addTextChangedListener {
            authViewModel.onEmailChange(it.toString())
        }

        boton_recuperar_contraseña.setOnClickListener {
            if (campo_correo_forget_password.text.toString() != ""){
                authViewModel.onResetPassword()
                lifecycleScope.launch {
                    authViewModel.authState.collectLatest { state ->
                        if (state is AuthState.Success) {
                            builder
                                .setMessage("Enlace enviado al correo")
                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        }
                    }
                }
            }else{
                builder
                    .setMessage("El campo no puede estar vacio")
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }

        enlace_recuerdas_contraseña.setOnClickListener{
            findNavController().navigate(R.id.action_forgetPassword_to_login)
        }
    }

}