package com.example.shopu.domain

import com.example.shopu.data.network.AuthenticationService
import com.example.shopu.data.network.UserService
import com.example.shopu.ui.register.model.UserRegister
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val userService: UserService
) {

    suspend operator fun invoke(userRegister: UserRegister): Boolean {
        val accountCreated =
            authenticationService.createAccount(userRegister.email, userRegister.password) != null
        return if (accountCreated) {
            userService.createUserTable(userRegister)
        } else {
            false
        }
    }
}