package com.example.shopu.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.shopu.R
import com.example.shopu.core.ex.dismissKeyboard
import com.example.shopu.core.ex.onTextChanged
import com.example.shopu.databinding.ActivityRegisterBinding
import com.example.shopu.ui.login.Login
import com.example.shopu.ui.register.model.UserRegister
import com.example.shopu.ui.verification.Verification
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Register : AppCompatActivity() {
    companion object {
        fun create(context: Context): Intent =
            Intent(context, Register::class.java)
    }

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.emailEdit.onTextChanged { onFieldChanged() }

        binding.nameEdit.onTextChanged { onFieldChanged() }

        binding.passwordEdit.onTextChanged { onFieldChanged() }

        binding.confirmPasswordEdit.onTextChanged { onFieldChanged() }

        with(binding) {
            buttonRegister.setOnClickListener {
                it.dismissKeyboard()
                registerViewModel.onSignInSelected(
                    UserRegister(
                        name = binding.nameEdit.text.toString(),
                        email = binding.emailEdit.text.toString(),
                        password = binding.passwordEdit.text.toString(),
                        passwordConfirmation = binding.confirmPasswordEdit.text.toString()
                    )
                )
            }
        }
    }

    private fun initObservers() {
        registerViewModel.navigateToVerifyEmail.observe(this) {
            it.getContentIfNotHandled()?.let {
                goToVerifyEmail()
            }
        }

        lifecycleScope.launchWhenStarted {
            registerViewModel.viewState.collect { viewState ->
                updateUI(viewState)
            }
        }

        registerViewModel.showErrorDialog.observe(this) { showError ->
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

    private fun updateUI(viewState: SignInViewState) {
        with(binding) {
            if (viewState.isValidEmail) null else emailEdit.setTextColor(getColor(R.color.red))
            if (viewState.isValidPassword) null else passwordEdit.setTextColor(getColor(R.color.red))
            if (viewState.isValidPassword) null else confirmPasswordEdit.setTextColor(getColor(R.color.red))
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            registerViewModel.onFieldsChanged(
                UserRegister(
                    name = binding.nameEdit.text.toString(),
                    email = binding.emailEdit.text.toString(),
                    password = binding.passwordEdit.text.toString(),
                    passwordConfirmation = binding.confirmPasswordEdit.text.toString()
                )
            )
        }
    }

    private fun goToVerifyEmail() {
        startActivity(Verification.create(this))
    }

}