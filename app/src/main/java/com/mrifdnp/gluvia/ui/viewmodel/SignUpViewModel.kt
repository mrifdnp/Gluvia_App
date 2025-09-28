package com.mrifdnp.gluvia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.mrifdnp.gluvia.data.AuthRepository

import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class SignUpViewModel(private val signUpRepository: AuthRepository) : ViewModel() {

    // Dipanggil dari Composable untuk navigasi setelah sukses
    lateinit var onNavigateToHome: () -> Unit

    // --- State UI untuk Supabase ---

    // State untuk menampilkan spinner loading
    var isLoading by mutableStateOf(false)
        private set

    // State untuk menampilkan pesan error ke pengguna
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // State untuk menampilkan dialog verifikasi email (opsional, tergantung alur Anda)
    var showVerificationRequired by mutableStateOf(false)
        private set

    // --- Definisi State Input ---
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
                usernameValue.length >= 4 && emailValue.contains("@") &&
                passwordValue.length >= 6 && selectedDate != null
    // Helper untuk memformat tanggal
    val dateText: String
        get() {
            val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id", "ID"))
            return selectedDate?.let {
                Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter)
            } ?: "Pilih tanggal"
        }
    var isOtpSent by mutableStateOf(false)
        private set

    var otpToken by mutableStateOf("")
        private set

    // Handler baru untuk input OTP
    fun onOtpChange(newOtp: String) {
        otpToken = newOtp.take(6) // Asumsi OTP 6 digit
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
        // 1. Cek validitas form sebelum memproses
        if (!isFormComplete) {
            errorMessage = "Silakan lengkapi semua bidang dengan benar."
            return
        }

        // Reset state
        isLoading = true
        errorMessage = null
        // showVerificationRequired = false // Tidak diperlukan lagi

        viewModelScope.launch {
            // 2. Panggil Repository di background
            val result = signUpRepository.signUp(
                email = emailValue,
                password = passwordValue,
                fullName = usernameValue
            )

            // 3. Update UI berdasarkan hasil
            isLoading = false // Hentikan loading terlepas dari hasilnya

            result.onSuccess {
                // 🔑 PENANGANAN UTAMA: Langsung navigasi ke Screen Home
                if (::onNavigateToHome.isInitialized) {
                    onNavigateToHome()
                }
            }.onFailure { exception ->
                // Pendaftaran gagal
                errorMessage = exception.message ?: "Terjadi kesalahan yang tidak diketahui saat mendaftar."
            }
        }
    }
}