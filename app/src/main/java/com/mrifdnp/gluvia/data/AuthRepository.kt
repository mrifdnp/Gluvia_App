package com.mrifdnp.gluvia.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import android.util.Log
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.IDTokenProvider
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.delay


private const val TAG = "AuthRepository" // 🔑 Ubah TAG

class AuthRepository(
    private val supabaseClient: SupabaseClient
) {
    suspend fun signUp(email: String, password: String, fullName: String): Result<Unit> {

        return withContext(Dispatchers.IO) {
            try {
                // Gunakan signUpWith(Email) dan sertakan user metadata
                supabaseClient.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password

                    // Data ini disimpan di kolom 'user_metadata' di tabel 'auth.users'.
                    data = buildJsonObject {
                        put("full_name", fullName)
                    }
                }

                Log.i(TAG, "Pendaftaran BERHASIL untuk $email. Menunggu verifikasi email.")
                // Mengembalikan Result.success(Unit) karena fungsi berhasil dieksekusi.
                return@withContext Result.success(Unit)
            } catch (e: Exception) {
                // Menangkap error dari Supabase (misalnya, email sudah terdaftar, password lemah)
                Log.e(TAG, "Pendaftaran GAGAL. Error: ${e.message}", e)
                return@withContext Result.failure(e)
            }
        }

    }
    suspend fun signIn(email: String, password: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Panggil fungsi signInWith(Email)
                // Jika kredensial salah, baris ini akan melempar Exception.
                supabaseClient.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                // 2. Jika kode mencapai titik ini, berarti LOGIN SUKSES
                // (Sesi sudah dibuat dan disimpan secara otomatis oleh SDK)
                Log.i(TAG, "Login BERHASIL untuk $email.")
                return@withContext Result.success(Unit)

            } catch (e: Exception) {
                // Menangkap error dari Supabase (Password salah, user tidak ditemukan, dll.)
                Log.e(TAG, "Login GAGAL. Error: ${e.message}", e)
                return@withContext Result.failure(e)
            }
        }
    }
    suspend fun signOut(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // 🔑 Panggil fungsi signOut (disediakan oleh GoTrue/Auth)
                supabaseClient.auth.signOut()

                Log.i(TAG, "Sign Out BERHASIL. Sesi telah dihapus.")
                return@withContext Result.success(Unit)

            } catch (e: Exception) {
                // Menangkap error jika sign out gagal
                Log.e(TAG, "Sign Out GAGAL. Error: ${e.message}", e)
                // Walaupun gagal, kita seringkali tetap mengarahkan ke layar login
                // karena sesi lokal kemungkinan sudah tidak valid.
                return@withContext Result.failure(e)
            }
        }
    }
    fun getAuthState(): Boolean {
        // 🔑 PERBAIKAN: Gunakan properti 'currentUserOrNull' yang mengembalikan objek User atau null
        return supabaseClient.auth.currentUserOrNull() != null
    }
    fun logActiveUser() {
        // 🔑 Mendapatkan objek User yang sedang login (nullable)
        val user = supabaseClient.auth.currentUserOrNull()

        if (user != null) {
            val userId = user.id
            // Ambil email, gunakan fallback jika email null
            val userEmail = user.email ?: "Email tidak tersedia"

            // Catat identitas pengguna yang sedang login
            Log.i(TAG, "👤 Pengguna Sesi Aktif: ID: $userId, Email: $userEmail")
        } else {
            Log.i(TAG, "🔴 Tidak ada sesi pengguna aktif yang ditemukan.")
        }
    }
    fun getCurrentUserId(): String? {
        // 🔑 Mengambil objek User yang sedang login, lalu mendapatkan properti ID-nya.
        return supabaseClient.auth.currentUserOrNull()?.id
    }


}

