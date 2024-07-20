package com.example.shopu.ui.forgetPassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shopu.R
import com.example.shopu.core.ex.dismissKeyboard
import com.example.shopu.core.ex.onTextChanged
import com.example.shopu.databinding.ActivityForgetPasswordBinding
import com.example.shopu.ui.confirmationRecoverPassword.ConfirmationRecoverPassword
import com.example.shopu.ui.login.Login
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPassword : AppCompatActivity() {

    companion object {
        fun create(context: Context): Intent =
            Intent(context, ForgetPassword::class.java)
    }

    private lateinit var binding: ActivityForgetPasswordBinding
    private val forgetPasswordViewModel: ForgetPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initListeners()
        initObservers()
    }

    private fun initListeners(){
        binding.campoCorreoForgetPassword.onTextChanged { onFieldChanged() }

        binding.enlaceRecuerdasContraseA.setOnClickListener { forgetPasswordViewModel.onLoginSelected() }

        with(binding) {
            botonRecuperarContraseA.setOnClickListener {
                it.dismissKeyboard()
                forgetPasswordViewModel.onRecoverPasswordSelected(
                    email = campoCorreoForgetPassword.text.toString()
                )
            }
        }
    }

    private fun initObservers(){
        forgetPasswordViewModel.navigateToConfirmationRecoverPassword.observe(this) {
            it.getContentIfNotHandled()?.let {
                goToConfirmationRecoverPassword()
            }
        }

        forgetPasswordViewModel.navigateToLogin.observe(this) {
            it.getContentIfNotHandled()?.let {
                goToLogin()
            }
        }

        lifecycleScope.launchWhenStarted {
            forgetPasswordViewModel.viewState.collect { viewState ->
                updateUI(viewState)
            }
        }

        forgetPasswordViewModel.showErrorDialog.observe(this) { showError ->
            if (showError) showErrorDialog()
        }
    }

    private fun showErrorDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.signin_error_dialog_title))
            .setMessage(resources.getString(R.string.signin_error_dialog_body))
            .setPositiveButton(resources.getString(R.string.signin_error_dialog_positive_action)) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun updateUI(viewState: ForgetPasswordViewState) {
        with(binding) {
            if (viewState.isValidEmail) null else campoCorreoForgetPassword.setTextColor(getColor(R.color.red))
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            forgetPasswordViewModel.onFieldsChanged(
                email = binding.campoCorreoForgetPassword.text.toString()
            )
        }
    }

    private fun goToConfirmationRecoverPassword() {
        startActivity(ConfirmationRecoverPassword.create(this))
    }

    private fun goToLogin() {
        startActivity(Login.create(this))
    }
}