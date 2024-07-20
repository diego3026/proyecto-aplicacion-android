package com.example.shopu.ui.register

data class RegisterViewState(
    val isLoading: Boolean = false,
    val isValidEmail: Boolean = true,
    val isValidPassword: Boolean = true,
    val isValidName: Boolean = true,
    val isValidContract: Boolean = true
){
    fun userValidated() = isValidEmail && isValidPassword && isValidName && isValidContract
}