package com.example.shopu.ui.forgetPassword

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopu.core.Event
import com.example.shopu.domain.RecoverPasswordUseCase
import com.example.shopu.ui.login.model.UserLogin
import com.example.shopu.ui.register.RegisterViewState
import com.example.shopu.ui.register.model.UserRegister
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(private val recoverPasswordUseCase: RecoverPasswordUseCase): ViewModel(){

    private val _navigateToConfirmationRecoverPassword = MutableLiveData<Event<Boolean>>()
    val navigateToConfirmationRecoverPassword: LiveData<Event<Boolean>>
        get() = _navigateToConfirmationRecoverPassword

    private val _navigateToLogin = MutableLiveData<Event<Boolean>>()
    val navigateToLogin: LiveData<Event<Boolean>>
        get() = _navigateToLogin

    private val _viewState = MutableStateFlow(ForgetPasswordViewState())
    val viewState: StateFlow<ForgetPasswordViewState>
        get() = _viewState

    private var _showErrorDialog = MutableLiveData(false)
    val showErrorDialog: LiveData<Boolean>
        get() = _showErrorDialog

    fun onLoginSelected() {
        _navigateToLogin.value = Event(true)
    }

    fun onRecoverPasswordSelected(email: String) {
        if (email.emailValidated() && email.isNotEmpty()) {
            recoverPassword(email)
        } else {
            onFieldsChanged(email)
        }
    }

    private fun recoverPassword(email: String) {
        viewModelScope.launch {
            _viewState.value = ForgetPasswordViewState(isLoading = true)
            val emailSended = recoverPasswordUseCase(email)
            if (emailSended as Boolean) {
                _navigateToConfirmationRecoverPassword.value = Event(true)
            } else {
                _showErrorDialog.value = true
            }
            _viewState.value = ForgetPasswordViewState(isLoading = false)
        }
    }

    fun onFieldsChanged(email: String) {
        _viewState.value = email.toSignInViewState()
    }

    private fun isValidOrEmptyEmail(email: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()

    private fun String.emailValidated() = isValidOrEmptyEmail(this)

    private fun String.toSignInViewState(): ForgetPasswordViewState {
        return ForgetPasswordViewState(
            isValidEmail = isValidOrEmptyEmail(this),
        )
    }
}