package com.example.shopu.ui.verification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.shopu.R
import com.example.shopu.databinding.ActivityVerificationEmailBinding
import com.example.shopu.ui.principal.Principal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerificationEmail : AppCompatActivity() {
    companion object {
        fun create(context: Context): Intent =
            Intent(context, VerificationEmail::class.java)
    }

    private lateinit var binding: ActivityVerificationEmailBinding
    private val verificationViewModel: VerificationEmailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.animation.setAnimation(R.raw.loading)
        initUI()
    }

    private fun initUI() {
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.btnVerification.setOnClickListener { verificationViewModel.onGoHome() }
    }

    private fun initObservers() {
        verificationViewModel.showContinueButton.observe(this) {
            it.getContentIfNotHandled()?.let { showButton ->
                binding.btnVerification.isVisible = showButton
            }
        }

        verificationViewModel.navigateToHome.observe(this) {
            it.getContentIfNotHandled()?.let {
                goToHome()
            }
        }
    }

    private fun goToHome() {
        startActivity(Principal.create(this))
        finish()
    }
}