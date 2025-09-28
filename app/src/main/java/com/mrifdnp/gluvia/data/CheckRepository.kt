// File: com.mrifdnp.gluvia.data/CheckRepository.kt

package com.mrifdnp.gluvia.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import io.github.jan.supabase.postgrest.rpc



@Serializable
data class MonthlyAverage(
    @SerialName("month_label")
    val monthLabel: String,
    @SerialName("average_glucose")
    val averageGlucose: Float // Tetap Float di Kotlin untuk memudahkan
)

class CheckRepository(
    private val supabaseClient: SupabaseClient,
    private val authRepository: AuthRepository
) {
    private val HISTORY_TABLE = "check_history"

    suspend fun saveCheckHistory(record: CheckRecord): Result<Unit> {
        return withContext(Dispatchers.IO) {
            val userId = authRepository.getCurrentUserId()
            if (userId == null) return@withContext Result.failure(Exception("Pengguna tidak login"))

            try {
                // 🔑 SOLUSI: Buat objek Payload yang sudah @Serializable
                val payload = CheckRecordPayload(
                    userId = userId, // String UUID dari auth
                    phValue = record.phValue,
                    glucoseMgdl = record.glucoseMgdl,
                    statusResult = record.statusResult
                )

                // Klien Postgrest DAPAT men-serialisasi List<SerializableClass>
                supabaseClient.postgrest[HISTORY_TABLE].insert(listOf(payload))

                Result.success(Unit)
            } catch (e: Exception) {
                // ...
                Result.failure(e)
            }
        }
    }
    suspend fun getMonthlyAverageGlucose(): Result<List<MonthlyAverage>> {
        return withContext(Dispatchers.IO) {
            val userId = authRepository.getCurrentUserId()
            if (userId == null) return@withContext Result.failure(Exception("User tidak terautentikasi"))

            try {
                // 🔑 SOLUSI RPC: Panggil fungsi database yang baru dibuat
                val rawData = supabaseClient.postgrest.rpc(
                    function = "get_monthly_glucose_avg", // Nama fungsi SQL
                    parameters = mapOf("user_uuid" to userId) // Kirim ID user
                ).decodeList<MonthlyAverage>() // Decode langsung ke List<MonthlyAverage>

                Result.success(rawData)
            } catch (e: Exception) {
                // Error pada RPC (misalnya, RLS salah, atau fungsi SQL error)
                Result.failure(e)
            }
        }
    }
}