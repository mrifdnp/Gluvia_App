// File: com.mrifdnp.gluvia.data/ProfileRepository.kt

package com.mrifdnp.gluvia.data

import android.util.Log
import com.mrifdnp.gluvia.data.AuthRepository // Untuk mendapatkan user ID
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID



class ProfileRepository(
    private val supabaseClient: SupabaseClient,
    private val authRepository: AuthRepository
) {
    private val TABLE_NAME = "profiles"
    private val AVATAR_BUCKET = "avatars"

    // 1. READ (Ambil Data Profil)
    suspend fun getProfile(): Result<Profile> {
        return withContext(Dispatchers.IO) {
            val userId = authRepository.getCurrentUserId() // Dapatkan ID pengguna aktif

            if (userId == null) {
                return@withContext Result.failure(Exception("Pengguna tidak login"))
            }

            try {
                val profile = supabaseClient.postgrest[TABLE_NAME]
                    .select {
                        filter { eq("id", userId) }
                        limit(1)
                    }
                    .decodeSingle<Profile>() // Ambil satu objek Profile

                Result.success(profile)
            } catch (e: RestException) {
                // Tangani kasus 404 (data tidak ditemukan) atau error lain
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // 2. UPDATE (Perbarui Deskripsi)
    suspend fun updateProfile(
        newUsername: String,
        newDescription: String,
        avatarBytes: ByteArray?
    ): Result<String?> = withContext(Dispatchers.IO) {
        val userId = authRepository.getCurrentUserId()
            ?: return@withContext Result.failure(Exception("Pengguna tidak login"))

        try {
            var avatarUrl: String? = null
            if (avatarBytes != null) {
                val fileName = "avatar_${userId}_${UUID.randomUUID()}.jpg"
                supabaseClient.storage.from(AVATAR_BUCKET).upload(fileName, avatarBytes)
                avatarUrl = supabaseClient.storage.from(AVATAR_BUCKET).publicUrl(fileName)
            }

            val updateData = ProfileUpdate(
                username = newUsername,
                description = newDescription,
                avatarUrl = avatarUrl
            )
            supabaseClient.postgrest[TABLE_NAME].update(updateData) {
                filter { eq("id", userId) }
            }

            Result.success(avatarUrl)
        } catch (e: Exception) {
            Log.e("ProfileRepository", "Gagal update profile", e)
            Result.failure(e)
        }
    }

}
