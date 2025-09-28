package com.mrifdnp.gluvia.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mrifdnp.gluvia.data.AuthRepository

// File: com.mrifdnp.gluvia.ui.viewmodel/MainViewModel.kt

enum class AppState { AUTHENTICATED, UNAUTHENTICATED }

class MainViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // State yang menentukan layar awal
    var appState by mutableStateOf<AppState?>(null)
        private set

    init {
        // Jalankan pemeriksaan segera saat ViewModel dibuat
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        // Pengecekan ini biasanya harus di dalam viewModelScope.launch{} jika authRepository.getAuthState() suspend
        // Jika tidak, jalankan synchronous saja.
        authRepository.logActiveUser()
        val isAuthenticated = authRepository.getAuthState()

        appState = if (isAuthenticated) {
            AppState.AUTHENTICATED
        } else {
            AppState.UNAUTHENTICATED
        }
        // Sekarang appState akan menjadi AUTHENTICATED atau UNAUTHENTICATED, bukan lagi null
    }
}