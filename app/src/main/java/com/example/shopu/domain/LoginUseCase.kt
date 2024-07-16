package com.example.shopu.domain

import com.example.shopu.data.network.AuthenticationService
import com.example.shopu.data.response.LoginResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authenticationService: AuthenticationService) {

    suspend operator fun invoke(email: String, password: String): LoginResult =
        authenticationService.login(email, password)
}