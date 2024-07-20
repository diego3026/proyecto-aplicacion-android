package com.example.shopu.ui.register.model

data class UserRegister(
    val name: String,
    val email: String,
    val password: String,
    val passwordConfirmation: String,
    val contract: Boolean
) {
    fun isNotEmpty() =
        name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && passwordConfirmation.isNotEmpty()
}