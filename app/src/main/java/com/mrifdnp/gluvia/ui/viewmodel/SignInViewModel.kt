package com.mrifdnp.gluvia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.mrifdnp.gluvia.data.AuthRepository // 🔑 Ganti ke AuthRepository
import kotlinx.coroutines.launch

class SignInViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // Dipanggil dari Composable untuk navigasi setelah sukses
    lateinit var onNavigateToHome: () -> Unit

    // --- State Input ---
    var emailValue by mutableStateOf("")
        private set
    var passwordValue by mutableStateOf("")
        private set

    // --- State UI ---
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set

    // --- Handler Input ---
    fun onEmailChange(newEmail: String) { emailValue = newEmail }
    fun onPasswordChange(newPassword: String) { passwordValue = newPassword }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }
    val isFormValid: Boolean
        get() = emailValue.isNotEmpty() && passwordValue.isNotEmpty()


    // --- Logika Login ---
    fun onSignInClicked() {
        // Tambahkan validasi dasar di sini (email, password tidak boleh kosong)
        if (emailValue.isEmpty() || passwordValue.isEmpty()) {
            errorMessage = "Email dan password tidak boleh kosong."
            return
        }

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            val result = authRepository.signIn(email = emailValue, password = passwordValue)

            isLoading = false

            result.onSuccess {
                // Login berhasil
                if (::onNavigateToHome.isInitialized) {
                    onNavigateToHome()
                }
            }.onFailure { exception ->
                // Login gagal (password salah, pengguna tidak ditemukan)
                errorMessage = "Gagal login: ${exception.message ?: "Periksa kredensial Anda."}"
            }
        }
    }
}