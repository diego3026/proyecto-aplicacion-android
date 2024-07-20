package com.example.shopu.ui.confirmationRecoverPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopu.core.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConfirmationRecoverPasswordViewModel @Inject constructor() : ViewModel() {

    private val _navigateToLogin= MutableLiveData<Event<Boolean>>()
    val navigateToLogin: LiveData<Event<Boolean>>
        get() = _navigateToLogin

    fun onLoginSelected(){
        _navigateToLogin.value = Event(true)
    }
}