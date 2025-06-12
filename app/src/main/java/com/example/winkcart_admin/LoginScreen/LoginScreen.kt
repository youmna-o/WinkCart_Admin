package com.example.winkcart_admin.LoginScreen

import android.content.res.Resources.Theme
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.winkcart_admin.Screens
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.ui.theme.Rose10
import com.example.winkcart_admin.ui.theme.Rose40
import com.example.winkcart_admin.ui.theme.Rose80

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel
) {
    val loginUiState by viewModel.loginUiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(loginUiState.status) {
        when (val status = loginUiState.status) {
            is ResponseStatus.Success<*> -> {
                navController.navigate(Screens.ProductsScr) {
                    popUpTo("login") { inclusive = true }
                }
            }
            is ResponseStatus.Error -> {
                Toast.makeText(context, "Login Failed: ${status.error.message}", Toast.LENGTH_LONG).show()
            }
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Admin Login",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (loginUiState.email.isNotEmpty() && !loginUiState.isEmailValid) {
                Text("Invalid email format", color = MaterialTheme.colorScheme.error)
            }
            OutlinedTextField(
                value = loginUiState.email,
                onValueChange = {viewModel.onEmailChange(it)},
                label = { Text("Email") },
                isError = loginUiState.email.isNotEmpty() && !loginUiState.isEmailValid,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "email",tint = Rose40) },
            )


            OutlinedTextField(
                value = loginUiState.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "password", tint = Rose40) }
            )

            Button(
                onClick = viewModel::onLoginClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = loginUiState.canLogin
            ) {
                if (loginUiState.status is ResponseStatus.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Login")
                }
            }
        }
    }
}
