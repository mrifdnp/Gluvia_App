package com.mrifdnp.gluvia.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.mrifdnp.gluvia.data.AuthRepository // 🔑 Ganti ke AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext

class SignInViewModel(private val authRepository: AuthRepository) : ViewModel() {
    val TAG = "GOOGLE_AUTH_INFO"

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
    fun signInWithGoogle(googleIdToken: String?, rawNonce: String) {
        if (googleIdToken.isNullOrBlank()) {
            errorMessage = "Google token kosong."
            return
        }

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                val supabase: SupabaseClient = GlobalContext.get().get()

                // Panggil signInWith IDToken provider
                supabase.auth.signInWith(IDToken) {
                    idToken = googleIdToken
                    provider = Google
                    nonce = rawNonce
                }

                // Pastikan sesi tersedia
                val user = supabase.auth.currentUserOrNull()
                if (user != null) {
                    Log.i(TAG, "Login Google sukses: id=${user.id} email=${user.email}")
                    if (::onNavigateToHome.isInitialized) onNavigateToHome()
                } else {
                    // Sukses panggilan ke API, tapi sdk belum mengisi sesi (jarang terjadi)
                    errorMessage = "Login berhasil tapi sesi belum tersedia."
                    Log.i(TAG, "Google sign-in: user==null setelah signInWith")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Google sign-in error", e)
                errorMessage = e.message ?: "Google sign-in gagal."
            } finally {
                isLoading = false
            }
        }
    }
}