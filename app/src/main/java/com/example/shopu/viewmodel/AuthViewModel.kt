package com.example.shopu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mercaditu.repository.AuthenticationRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onNameChange(name: String) {
        _name.value = name
    }

    fun onSignIn() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authenticationRepository.signIn(
                email = _email.value,
                password = _password.value
            )
            _authState.value = if (result) AuthState.Success else AuthState.Error("Sign in failed")
        }
    }

    fun onGoogleSignIn() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authenticationRepository.signInWithGoogle()
            _authState.value = if (result) AuthState.Success else AuthState.Error("Google sign in failed")
        }
    }

    fun onSignUp() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authenticationRepository.signUp(
                email = _email.value,
                password = _password.value,
                name = _name.value
            )
            _authState.value = if (result) AuthState.Success else AuthState.Error("Sign up failed")
        }
    }

    fun onSignOut() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authenticationRepository.logOut()
            _authState.value = if (result) AuthState.Success else AuthState.Error("Sign out failed")
        }
    }

    fun onResetPassword() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authenticationRepository.resetPassword(
                email = _email.value
            )
            _authState.value = if (result) AuthState.Success else AuthState.Error("Reset password failed")
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}
