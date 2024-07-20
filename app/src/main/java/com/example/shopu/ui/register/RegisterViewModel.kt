package com.example.shopu.ui.register

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopu.core.Event
import com.example.shopu.domain.CreateAccountUseCase
import com.example.shopu.domain.DownloadFileUseCase
import com.example.shopu.ui.register.model.UserRegister
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(val createAccountUseCase: CreateAccountUseCase, val downloadFileUseCase: DownloadFileUseCase) :
    ViewModel() {

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
        const val TAG = "RegisterViewModel"
    }

    private val _navigateToVerifyEmail = MutableLiveData<Event<Boolean>>()
    val navigateToVerifyEmail: LiveData<Event<Boolean>>
        get() = _navigateToVerifyEmail

    private val _viewState = MutableStateFlow(RegisterViewState())
    val viewState: StateFlow<RegisterViewState>
        get() = _viewState

    private var _showErrorDialog = MutableLiveData(false)
    val showErrorDialog: LiveData<Boolean>
        get() = _showErrorDialog

    private val _downloadResult = MutableLiveData<Event<Result<String>>>()
    val downloadResult: LiveData<Event<Result<String>>> get() = _downloadResult

    fun onTermsAndConditionsClicked(url: String) {
        viewModelScope.launch {
            try {
                val result = downloadFileUseCase(url)
                _downloadResult.value = Event(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onPrivacyPolicyClicked(url: String) {
        viewModelScope.launch {
            try {
                val result = downloadFileUseCase(url)
                _downloadResult.value = Event(result)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun onSignInSelected(userRegister: UserRegister) {
        Log.d(TAG, "onSignInSelected called with userRegister: $userRegister")
        val viewState = userRegister.toSignInViewState()
        if (viewState.userValidated() && userRegister.isNotEmpty()) {
            Log.d(TAG, "User validated, proceeding to sign in")
            signInUser(userRegister)
        } else {
            Log.d(TAG, "User not validated, updating fields")
            onFieldsChanged(userRegister)
        }
    }

    private fun signInUser(userRegister: UserRegister) {
        viewModelScope.launch {
            Log.d(TAG, "signInUser called with userRegister: $userRegister")
            _viewState.value = RegisterViewState(isLoading = true)
            val accountCreated = createAccountUseCase(userRegister)
            Log.d(TAG, "Account creation result: $accountCreated")
            if (accountCreated) {
                Log.d(TAG, "Account created successfully, navigating to verify email")
                _navigateToVerifyEmail.value = Event(true)
            } else {
                Log.e(TAG, "Account creation failed, showing error dialog")
                _showErrorDialog.value = true
            }
            _viewState.value = RegisterViewState(isLoading = false)
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

    private fun isValidContract(contract: Boolean): Boolean = contract

    private fun UserRegister.toSignInViewState(): RegisterViewState {
        return RegisterViewState(
            isValidEmail = isValidOrEmptyEmail(email),
            isValidPassword = isValidOrEmptyPassword(password, passwordConfirmation),
            isValidName = isValidName(name),
            isValidContract = isValidContract(contract)
        )
    }
}