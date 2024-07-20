package com.example.shopu.ui.confirmationRecoverPassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.shopu.core.ex.dismissKeyboard
import com.example.shopu.databinding.ActivityConfirmationRecoverPasswordBinding
import com.example.shopu.ui.login.Login
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmationRecoverPassword : AppCompatActivity() {
    companion object {
        fun create(context: Context): Intent =
            Intent(context, ConfirmationRecoverPassword::class.java)
    }

    private lateinit var binding: ActivityConfirmationRecoverPasswordBinding
    private val confirmationRecoverPasswordViewModel: ConfirmationRecoverPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationRecoverPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initUI() {
        initListeners()
        initObservers()
    }

    private fun initListeners(){
        with(binding) {
            btnLogin.setOnClickListener {
                it.dismissKeyboard()
                confirmationRecoverPasswordViewModel.onLoginSelected()
            }
        }
    }

    private fun initObservers(){
        confirmationRecoverPasswordViewModel.navigateToLogin.observe(this) {
            it.getContentIfNotHandled()?.let {
                goToLogin()
            }
        }
    }

    private fun goToLogin() {
        startActivity(Login.create(this))
    }
}