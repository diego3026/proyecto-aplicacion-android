package com.example.shopu.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.shopu.R
import com.example.shopu.core.ex.dismissKeyboard
import com.example.shopu.core.ex.onCheckedChanged
import com.example.shopu.core.ex.onTextChanged
import com.example.shopu.databinding.ActivityRegisterBinding
import com.example.shopu.ui.register.model.UserRegister
import com.example.shopu.ui.verification.VerificationEmail
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

        val checkBox = binding.checkBox
        setupCheckBox(checkBox)

        initUI()
    }

    private fun setupCheckBox(checkBox: CheckBox)  {
        val text = "He leído y acepto los Términos y condiciones, y la Política de privacidad."
        val spannableString = SpannableString(text)

        val termsAndConditionsStart = text.indexOf("Términos y condiciones")
        val termsAndConditionsEnd = termsAndConditionsStart + "Términos y condiciones".length
        val privacyPolicyStart = text.indexOf("Política de privacidad")
        val privacyPolicyEnd = privacyPolicyStart + "Política de privacidad".length

        val termsClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                registerViewModel.onTermsAndConditionsClicked(R.string.terminos_y_condiciones.toString())
            }
        }

        val privacyClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                registerViewModel.onPrivacyPolicyClicked(R.string.politicas_de_privacidad.toString())
            }
        }

        spannableString.setSpan(
            termsClickableSpan,
            termsAndConditionsStart,
            termsAndConditionsEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(getColor(R.color.naranja)),
            termsAndConditionsStart,
            termsAndConditionsEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            privacyClickableSpan,
            privacyPolicyStart,
            privacyPolicyEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(getColor(R.color.naranja)),
            privacyPolicyStart,
            privacyPolicyEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        checkBox.text = spannableString
        checkBox.movementMethod = LinkMovementMethod.getInstance()
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

        binding.checkBox.onCheckedChanged { onFieldChanged() }

        with(binding) {
            buttonRegister.setOnClickListener {
                it.dismissKeyboard()
                Log.d("Register", "Register button clicked")
                registerViewModel.onSignInSelected(
                    UserRegister(
                        name = binding.nameEdit.text.toString(),
                        email = binding.emailEdit.text.toString(),
                        password = binding.passwordEdit.text.toString(),
                        passwordConfirmation = binding.confirmPasswordEdit.text.toString(),
                        contract = binding.checkBox.isChecked
                    )
                )
            }
        }
    }

    private fun initObservers() {
        registerViewModel.navigateToVerifyEmail.observe(this) {
            it.getContentIfNotHandled()?.let {
                Log.d("Register", "Navigating to verify email")
                goToVerifyEmail()
            }
        }

        lifecycleScope.launchWhenStarted {
            registerViewModel.viewState.collect { viewState ->
                Log.d("Register", "ViewState updated: $viewState")
                updateUI(viewState)
            }
        }

        registerViewModel.showErrorDialog.observe(this) { showError ->
            if (showError) {
                Log.e("Register", "Showing error dialog")
                showErrorDialog()
            }
        }

        registerViewModel.downloadResult.observe(this) { event ->
            event.getContentIfNotHandled()?.let { result ->
                result.onSuccess {
                    Log.d("Register", "Download successful: $it")
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }.onFailure {
                    Log.e("Register", "Download failed: ${it.message}")
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
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

    private fun updateUI(viewState: RegisterViewState) {
        with(binding) {
            pbLoading.isVisible = viewState.isLoading
            emailEdit.setTextColor(if (viewState.isValidEmail) ContextCompat.getColor(this@Register, R.color.black) else ContextCompat.getColor(this@Register, R.color.red))
            passwordEdit.setTextColor(if (viewState.isValidPassword) ContextCompat.getColor(this@Register, R.color.black) else ContextCompat.getColor(this@Register, R.color.red))
            confirmPasswordEdit.setTextColor(if (viewState.isValidPassword) ContextCompat.getColor(this@Register, R.color.black) else ContextCompat.getColor(this@Register, R.color.red))
            if (!viewState.isValidEmail){
                binding.textNotMatch.isVisible = true
            }else{
                binding.textNotMatch.isVisible = false
            }
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            registerViewModel.onFieldsChanged(
                UserRegister(
                    name = binding.nameEdit.text.toString(),
                    email = binding.emailEdit.text.toString(),
                    password = binding.passwordEdit.text.toString(),
                    passwordConfirmation = binding.confirmPasswordEdit.text.toString(),
                    contract = binding.checkBox.isChecked
                )
            )
        }
    }

    private fun goToVerifyEmail() {
        Log.d("Register", "Starting VerificationRegister activity")
        startActivity(VerificationEmail.create(this))
    }

}