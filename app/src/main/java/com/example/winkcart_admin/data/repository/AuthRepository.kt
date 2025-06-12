package com.example.winkcart_admin.data.repository

import com.example.winkcart_admin.data.ResponseStatus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepository  (
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun login(email: String, password: String) = withContext(Dispatchers.IO) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }
    
}
