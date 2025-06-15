package com.example.winkcart_admin.ProfileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.winkcart_admin.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _loggedInState = MutableStateFlow(false)
    val loggedInState=_loggedInState.asStateFlow()
    init {
        isAdminLoggedIn()
    }
    private fun isAdminLoggedIn(){
        viewModelScope.launch {
            _loggedInState.value=authRepository.isAdminLoggedIn()
        }
    }

    fun logOut() {
        viewModelScope.launch {
            authRepository.logoutAdmin()
            isAdminLoggedIn()
        }
    }


}

