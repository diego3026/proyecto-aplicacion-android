package com.example.shopu.ui.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopu.core.Event
import com.example.shopu.domain.CreateAccountUseCase
import com.example.shopu.ui.register.model.UserRegister
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(val createAccountUseCase: CreateAccountUseCase) :
    ViewModel() {

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }

    private val _navigateToVerifyEmail = MutableLiveData<Event<Boolean>>()
    val navigateToVerifyEmail: LiveData<Event<Boolean>>
        get() = _navigateToVerifyEmail

    private val _viewState = MutableStateFlow(SignInViewState())
    val viewState: StateFlow<SignInViewState>
        get() = _viewState

    private var _showErrorDialog = MutableLiveData(false)
    val showErrorDialog: LiveData<Boolean>
        get() = _showErrorDialog

    fun onSignInSelected(userRegister: UserRegister) {
        val viewState = userRegister.toSignInViewState()
        if (viewState.userValidated() && userRegister.isNotEmpty()) {
            signInUser(userRegister)
        } else {
            onFieldsChanged(userRegister)
        }
    }

    private fun signInUser(userRegister: UserRegister) {
        viewModelScope.launch {
            _viewState.value = SignInViewState(isLoading = true)
            val accountCreated = createAccountUseCase(userRegister)
            if (accountCreated) {
                _navigateToVerifyEmail.value = Event(true)
            } else {
                _showErrorDialog.value = true
            }
            _viewState.value = SignInViewState(isLoading = false)
        }
    }

    fun onFieldsChanged(userRegister: UserRegister) {
        _viewState.value = userRegister.toSignInViewState()
    }

    private fun isValidOrEmptyEmail(email: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()

    private fun isValidOrEmptyPassword(password: String, passwordConfirmation: String): Boolean =
        (password.length >= MIN_PASSWORD_LENGTH && password == passwordConfirmation) || password.isEmpty() || passwordConfirmation.isEmpty()

    private fun isValidName(name: String): Boolean =
        name.length >= MIN_PASSWORD_LENGTH || name.isEmpty()

    private fun UserRegister.toSignInViewState(): SignInViewState {
        return SignInViewState(
            isValidEmail = isValidOrEmptyEmail(email),
            isValidPassword = isValidOrEmptyPassword(password, passwordConfirmation),
            isValidName = isValidName(name)
        )
    }
}