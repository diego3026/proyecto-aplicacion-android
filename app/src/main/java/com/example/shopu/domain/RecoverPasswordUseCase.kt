package com.example.shopu.domain

import com.example.shopu.data.network.AuthenticationService
import com.example.shopu.ui.register.model.UserRegister
import javax.inject.Inject

class RecoverPasswordUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
) {
    suspend operator fun invoke(email: String) =
        authenticationService.recoverPassword(email)
}