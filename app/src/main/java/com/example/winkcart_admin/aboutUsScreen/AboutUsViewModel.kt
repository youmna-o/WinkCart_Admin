package com.example.winkcart_admin.aboutUsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.winkcart_admin.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutUsViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

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
/*
class AboutUsViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AboutUsViewModel(authRepository = repository) as T
    }
}*/
