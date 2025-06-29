package com.example.winkcart_admin.LoginScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_admin.CouponsScreen.CouponsViewModel
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.data.repository.AuthRepository
import com.example.winkcart_admin.data.repository.ProductRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    fun onEmailChange(email: String) {
        _loginUiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _loginUiState.update { it.copy(password = password) }
    }

    fun onLoginClick() {
        val currentState = _loginUiState.value
        if (!currentState.canLogin) return
        viewModelScope.launch {
            _loginUiState.update { it.copy(status = ResponseStatus.Loading) }
            try {
                authRepository.login(currentState.email, currentState.password)
                Log.i("TAG", "onLoginClick:called ")
                _loginUiState.update { it.copy(status = ResponseStatus.Success(Unit))}
            }catch(ex:Exception){
                _loginUiState.update { it.copy(status = ResponseStatus.Error(ex))}
            }
        }
    }

}
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val status: ResponseStatus<Unit>? = null
) {
    val isEmailValid: Boolean
        get() = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    val canLogin: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && isEmailValid
}
