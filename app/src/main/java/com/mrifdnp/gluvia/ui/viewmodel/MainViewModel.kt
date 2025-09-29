package com.mrifdnp.gluvia.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mrifdnp.gluvia.data.AuthRepository

// File: com.mrifdnp.gluvia.ui.viewmodel/MainViewModel.kt

enum class AppState { AUTHENTICATED, UNAUTHENTICATED,    ONBOARDING_REQUIRED,
}

class MainViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // State yang menentukan layar awal
    var appState by mutableStateOf<AppState?>(null)
        private set


    init {
        // Panggil fungsi yang akan mengecek status otentikasi dan onboarding
        checkAuthStatusAndOnboarding()
    }

    private fun checkAuthStatusAndOnboarding() {

        authRepository.logActiveUser()

        // 1. 🔑 PRIORITAS TERTINGGI: Cek Status Autentikasi
        val isAuthenticated = authRepository.getAuthState()

        if (isAuthenticated) {
            appState = AppState.AUTHENTICATED
            return // Jika sudah login, langsung ke Home dan abaikan Onboarding/Auth.
        }

        // 2. Cek Status Onboarding (Hanya jika belum login/UNAUTHENTICATED)
        // Asumsi: Anda harus menambahkan fungsi ini ke AuthRepository.
        val isOnboardingComplete = false // Ganti ini dengan authRepository.isOnboardingComplete() di aplikasi nyata

        if (!isOnboardingComplete) {
            appState = AppState.ONBOARDING_REQUIRED // Jika belum login DAN onboarding belum selesai
            return
        }

        // 3. Status Default (Hanya jika belum login dan onboarding sudah selesai)
        appState = AppState.UNAUTHENTICATED // Navigasi ke AuthScreen (login/register)
    }

    // Dipanggil dari Composable setelah onboarding selesai
    fun onOnboardingCompleted() {
        // Di sini kita hanya perlu memperbarui state agar tidak kembali ke onboarding
        // Di aplikasi nyata: Anda akan menyimpan flag persistence di sini.

        // Setelah onboarding selesai, rute selanjutnya adalah layar login
        appState = AppState.UNAUTHENTICATED
    }
}