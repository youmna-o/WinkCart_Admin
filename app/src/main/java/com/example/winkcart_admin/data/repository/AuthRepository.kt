package com.example.winkcart_admin.data.repository

import com.example.winkcart_admin.data.ResponseStatus
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AuthRepository @Inject constructor (
    private val firebaseAuth: FirebaseAuth,
    ) {
    suspend fun login(email: String, password: String) = withContext(Dispatchers.IO) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    fun isAdminLoggedIn(): Boolean {
        return firebaseAuth.currentUser!=null
    }

    suspend fun logoutAdmin() = withContext(Dispatchers.IO) {
        firebaseAuth.signOut()
    }
}
