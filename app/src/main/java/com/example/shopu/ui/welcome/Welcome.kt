package com.example.shopu.ui.welcome

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.shopu.R
import com.example.shopu.databinding.ActivityWelcomeBinding
import com.example.shopu.ui.login.Login

class Welcome : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private val welcomeViewModel: WelcomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        with(binding) {
            botonBienvenida.setOnClickListener { welcomeViewModel.onLoginSelected() }
        }
    }


    private fun initObservers() {
        welcomeViewModel.navigateToLogin.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                goToLogin()
            }
        })
    }

    private fun goToLogin() {
        startActivity(Login.create(this))
    }
}