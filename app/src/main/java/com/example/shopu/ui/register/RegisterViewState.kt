package com.example.shopu.ui.register

data class SignInViewState(
    val isLoading: Boolean = false,
    val isValidEmail: Boolean = true,
    val isValidPassword: Boolean = true,
    val isValidName: Boolean = true,
){
    fun userValidated() = isValidEmail && isValidPassword && isValidName
}