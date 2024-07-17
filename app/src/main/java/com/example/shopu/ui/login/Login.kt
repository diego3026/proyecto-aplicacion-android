package com.example.shopu.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shopu.R
import com.example.shopu.core.ex.dismissKeyboard
import com.example.shopu.core.ex.onTextChanged
import com.example.shopu.databinding.ActivityLoginBinding
import com.example.shopu.ui.login.model.UserLogin
import com.example.shopu.ui.register.Register
import com.example.shopu.ui.verification.Verification
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login : AppCompatActivity() {
    companion object {
        fun create(context: Context): Intent =
            Intent(context, Login::class.java)
    }

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.campoEmail.onTextChanged { onFieldChanged() }

        binding.campoContraseA.onTextChanged { onFieldChanged() }

        binding.olvidasteEnlace.setOnClickListener { loginViewModel.onForgotPasswordSelected() }

        binding.registrateEnlace.setOnClickListener { loginViewModel.onSignInSelected() }

        binding.botonLogin.setOnClickListener {
            it.dismissKeyboard()
            loginViewModel.onLoginSelected(
                binding.campoEmail.text.toString(),
                binding.campoContraseA.text.toString()
            )
        }
    }

    private fun initObservers() {

        //ir a register
        loginViewModel.navigateToSignIn.observe(this) {
            it.getContentIfNotHandled()?.let {
                goToSignIn()
            }
        }

        //ir a forgot password
        loginViewModel.navigateToForgotPassword.observe(this) {
            it.getContentIfNotHandled()?.let {
                goToForgotPassword()
            }
        }

        //ir a verify account
        loginViewModel.navigateToVerifyAccount.observe(this) {
            it.getContentIfNotHandled()?.let {
                goToVerify()
            }
        }

        loginViewModel.showErrorDialog.observe(this) { userLogin ->
            if (userLogin.showErrorDialog) showErrorDialog(userLogin)
        }

        lifecycleScope.launchWhenStarted {
            loginViewModel.viewState.collect { viewState ->
                updateUI(viewState)
            }
        }
    }

    private fun updateUI(viewState: LoginViewState) {
        with(binding) {
            pbLoading.visibility = Integer.valueOf(viewState.isLoading.toString())
            if (!viewState.isValidEmail) {
                campoEmail.setTextColor(getColor(R.color.red))
            }
            if (!viewState.isValidPassword) {
                campoContraseA.setTextColor(getColor(R.color.red))
            }
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            loginViewModel.onFieldsChanged(
                email = binding.campoEmail.text.toString(),
                password = binding.campoContraseA.text.toString()
            )
        }
    }

    private fun showErrorDialog(userLogin: UserLogin) {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.login_error_dialog_title))
            .setMessage(resources.getString(R.string.login_error_dialog_body))
            .setNegativeButton(resources.getString(R.string.login_error_dialog_negative_action)) { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.login_error_dialog_positive_action)) { dialog, which ->
                loginViewModel.onLoginSelected(
                    userLogin.email,
                    userLogin.password
                )
            }
            .show()
    }

    private fun goToForgotPassword() {
        startActivity(ForgetPassword.create(this))
    }

    private fun goToSignIn() {
        startActivity(Register.create(this))
    }

    private fun goToVerify() {
        startActivity(Verification.create(this))
    }
}