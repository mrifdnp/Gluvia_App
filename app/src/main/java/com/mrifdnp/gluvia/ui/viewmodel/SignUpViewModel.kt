package com.mrifdnp.gluvia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class SignUpViewModel : ViewModel() {
    lateinit var onNavigateToHome: () -> Unit

    // --- Definisi State ---

    // Gunakan StateFlow jika Anda ingin mengelola state di luar composable
    // Tapi untuk contoh sederhana ini, kita bisa pakai mutableStateOf di ViewModel
    var usernameValue by mutableStateOf("")
        private set
    var emailValue by mutableStateOf("")
        private set
    var passwordValue by mutableStateOf("")
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set

    var selectedDate by mutableStateOf<Long?>(null)
        private set

    var selectedGender by mutableStateOf("Pria")
        private set

    val genderOptions = listOf("Pria", "Wanita", "Lainnya")


    // --- Logika Validasi (Derived State) ---

    // State validitas yang akan di-consume oleh UI untuk menampilkan error
    val isUsernameValid: Boolean
        get() = usernameValue.length >= 4 || usernameValue.isEmpty()

    val isEmailValid: Boolean
        get() = emailValue.contains("@") || emailValue.isEmpty()

    val isPasswordValid: Boolean
        get() = passwordValue.length >= 6 || passwordValue.isEmpty()

    val isFormComplete: Boolean
        get() = isUsernameValid && isEmailValid && isPasswordValid &&
                usernameValue.isNotEmpty() && emailValue.isNotEmpty() &&
                passwordValue.isNotEmpty() && selectedDate != null

    // Helper untuk memformat tanggal
    val dateText: String
        get() {
            val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id", "ID"))
            return selectedDate?.let {
                Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter)
            } ?: "Pilih tanggal"
        }

    // --- Event Handler (Logika Bisnis) ---

    fun onUsernameChange(newUsername: String) {
        usernameValue = newUsername
    }

    fun onEmailChange(newEmail: String) {
        emailValue = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        passwordValue = newPassword
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    fun onDateSelected(newDateMillis: Long?) {
        selectedDate = newDateMillis
    }

    fun onGenderSelected(newGender: String) {
        selectedGender = newGender
    }

    fun onSignUpClicked() {
        // Logika pendaftaran ke backend/database
        if (isFormComplete) {
            println("Pendaftaran berhasil untuk: $usernameValue, $emailValue")
            if (::onNavigateToHome.isInitialized) {
                onNavigateToHome() // <-- Pemanggilan di sini
            }
        }
    }
}