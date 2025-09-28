// File: com.mrifdnp.gluvia.data/ProfileRepository.kt

package com.mrifdnp.gluvia.data

import com.mrifdnp.gluvia.data.AuthRepository // Untuk mendapatkan user ID
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.exceptions.RestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepository(
    private val supabaseClient: SupabaseClient,
    private val authRepository: AuthRepository
) {
    private val TABLE_NAME = "profiles"

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
    suspend fun updateDescription(newDescription: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            val userId = authRepository.getCurrentUserId()
            if (userId == null) return@withContext Result.failure(Exception("Pengguna tidak login"))

            try {
                supabaseClient.postgrest[TABLE_NAME]
                    .update(
                        value = mapOf("description" to newDescription)
                    ) {
                        filter { eq("id", userId) }
                    }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
// Catatan: Anda harus menambahkan getCurrentUserId() ke AuthRepository!