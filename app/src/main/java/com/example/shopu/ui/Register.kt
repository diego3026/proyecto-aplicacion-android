package com.example.shopu.ui

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mercaditu.di.SupabaseClient
import com.example.mercaditu.repository.AuthenticationRepository
import com.example.mercaditu.repository.implementation.AuthenticationRepositoryImpl
import com.example.shopu.R
import com.example.shopu.viewmodel.AuthState
import com.example.shopu.viewmodel.AuthViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class Register : Fragment() {
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_register, container, false)

        val checkBox: CheckBox = root.findViewById(R.id.checkBox)

        val termsText = "Términos y condiciones"
        val privacyText = "Política de privacidad"
        val fullText = "He leído y acepto los $termsText y la $privacyText."

        val spannableString = SpannableString(fullText)

        // Configura el enlace para los términos y condiciones
        val termsClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Aquí pones la URL a los términos y condiciones
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ejemplo.com/terminos"))
                startActivity(intent)
            }
        }

        // Configura el enlace para la política de privacidad
        val privacyClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Aquí pones la URL a la política de privacidad
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ejemplo.com/privacidad"))
                startActivity(intent)
            }
        }

        val color = context?.let { ContextCompat.getColor(it,R.color.naranja) }
        val termsStart = fullText.indexOf(termsText)
        val termsEnd = termsStart + termsText.length
        spannableString.setSpan(termsClickableSpan, termsStart, termsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(color?.let { ForegroundColorSpan(it) }, termsStart, termsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val privacyStart = fullText.indexOf(privacyText)
        val privacyEnd = privacyStart + privacyText.length
        spannableString.setSpan(privacyClickableSpan, privacyStart, privacyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(color?.let { ForegroundColorSpan(it) }, privacyStart, privacyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        checkBox.text = spannableString
        checkBox.movementMethod = LinkMovementMethod.getInstance()

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val supabaseClient = SupabaseClient()
        val auth = supabaseClient.getAuth()
        val authenticationRepository: AuthenticationRepository = AuthenticationRepositoryImpl(auth)
        authViewModel = AuthViewModel(authenticationRepository)

        val name_edit = view.findViewById<EditText>(R.id.name_edit)
        val email_edit = view.findViewById<EditText>(R.id.name_edit)
        val password_edit = view.findViewById<TextInputEditText>(R.id.password_edit)
        val confirm_password_edit = view.findViewById<TextInputEditText>(R.id.confirm_password_edit)

        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
        val button_register = view.findViewById<Button>(R.id.button_register)
        val button_google = view.findViewById<Button>(R.id.button_google)
        val text_not_match = view.findViewById<TextView>(R.id.text_not_match)

        confirm_password_edit.addTextChangedListener{
            if (password_edit?.text.toString() != confirm_password_edit?.text.toString()){
                text_not_match?.visibility = View.VISIBLE
            }else{
                text_not_match?.visibility = View.INVISIBLE
            }
        }

        lifecycleScope.launch {
            authViewModel.email.collect { email ->
                if (email_edit.text.toString() != email) {
                    email_edit.setText(email)
                }
            }
        }

        lifecycleScope.launch {
            authViewModel.name.collect { name ->
                if (name_edit.text.toString() != name) {
                    name_edit.setText(name)
                }
            }
        }

        lifecycleScope.launch {
            authViewModel.password.collect { password ->
                if (password_edit.text.toString() != password) {
                    password_edit.setText(password)
                }
            }
        }

        email_edit.addTextChangedListener {
            authViewModel.onEmailChange(it.toString())
        }

        password_edit.addTextChangedListener {
            authViewModel.onPasswordChange(it.toString())
        }

        name_edit.addTextChangedListener {
            authViewModel.onNameChange(it.toString())
        }

        button_register.setOnClickListener {
            signUp(checkBox.isChecked, name_edit.text.toString(),password_edit.text.toString(),confirm_password_edit.text.toString())
        }
    }

    fun signUp(checked: Boolean, name: String, password: String, confirmPassword: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        if (checked){
            if (password == confirmPassword){
                if (name == ""){
                    builder
                        .setMessage("El nombre no puede estar vacio")
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }else{
                    authViewModel.onSignUp()
                    lifecycleScope.launch {
                        authViewModel.authState.collectLatest { state ->
                            if (state is AuthState.Success) {
                                builder
                                    .setMessage("Registro exitoso")
                                val dialog: AlertDialog = builder.create()
                                dialog.show()
                                findNavController().navigate(R.id.action_register_to_home2)
                            }
                        }
                    }

                }
            }else{
                builder
                    .setMessage("Las contraseñas no coinciden")
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }else{
            builder
                .setMessage("Para continuar acepta los terminos y condiciones")

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
}