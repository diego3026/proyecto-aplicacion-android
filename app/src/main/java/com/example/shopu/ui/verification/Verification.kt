package com.example.shopu.ui.verification

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shopu.R
import com.example.shopu.databinding.ActivityVerificationBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Verification : AppCompatActivity() {
    private lateinit var binding: ActivityVerificationBinding
    private val verificationViewModel: VerificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initListeners()
        initObservers()
    }

    private fun initListeners() {
//        binding.btnGoToDetail.setOnClickListener { verificationViewModel.onGoToDetailSelected() }
    }

    private fun initObservers() {
//        verificationViewModel.navigateToVerifyAccount.observe(this) {
//            it.getContentIfNotHandled()?.let {
//                LoginSuccessDialog.create().show(dialogLauncher, this)
//            }
//        }
//
//        verificationViewModel.showContinueButton.observe(this) {
//            it.getContentIfNotHandled()?.let {
//                binding.btnGoToDetail.isVisible = true
//            }
//        }
    }
}