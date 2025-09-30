// File: com.mrifdnp.gluvia.ui.viewmodel/ProfileViewModel.kt

package com.mrifdnp.gluvia.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.mrifdnp.gluvia.data.ProfileRepository // 🔑 Import Repository baru
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() { // 🔑 Terima Repository

    var username by mutableStateOf("Memuat...")
        private set

    var profileImageUrl by mutableStateOf<String?>(null)
        private set

    var profileCreationDate by mutableStateOf("Memuat...")
        private set

    var description by mutableStateOf("Memuat deskripsi...")
        private set

    var isLoading by mutableStateOf(true)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    val profileImageResId = com.mrifdnp.gluvia.R.drawable.sakuchang // Placeholder

    var calculatedGlukosa by mutableStateOf<Float?>(null)
        private set
    var isCalculated by mutableStateOf(false)
        private set

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            profileRepository.getProfile().onSuccess { profile ->
                username = profile.username
                description = profile.description ?: "Belum ada deskripsi."
                profileImageUrl = profile.avatarUrl
                // Format tanggal dari Supabase (contoh: ISO 8601)
                profileCreationDate = profile.createdAt?.let { isoTime ->
                    try {
                        val zdt = ZonedDateTime.parse(isoTime)
                        zdt.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id", "ID")))
                    } catch (e: Exception) {
                        "Tanggal tidak valid"
                    }
                } ?: "Tidak diketahui"

            }.onFailure { e ->
                Log.e("ProfileViewModel", "Gagal memuat profil Supabase. Pesan: ${e.message}", e)
                errorMessage = "Gagal memuat profil: ${e.message}"
            }
            isLoading = false
        }
    }

    fun onEditProfileClicked() {
        // Logika untuk menavigasi ke Edit Profile Screen
        println("Navigasi ke Edit Profile")
    }

    // Fungsi untuk Update
    fun saveNewDescription(newDescription: String) {
        viewModelScope.launch {
            isLoading = true
            profileRepository.updateDescription(newDescription).onSuccess {
                description = newDescription // Update UI lokal
            }.onFailure { e ->
                errorMessage = "Gagal menyimpan: ${e.message}"
            }
            isLoading = false
        }
    }

}