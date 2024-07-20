package com.example.shopu.ui.forgetPassword

data class ForgetPasswordViewState(
    val isLoading: Boolean = false,
    val isValidEmail: Boolean = true,
){
    fun validated() = isValidEmail
}