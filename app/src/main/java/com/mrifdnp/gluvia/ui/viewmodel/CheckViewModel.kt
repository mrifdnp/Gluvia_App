// File: com.mrifdnp.gluvia.ui.viewmodel/CheckViewModel.kt

package com.mrifdnp.gluvia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.mrifdnp.gluvia.data.CheckRecord
import com.mrifdnp.gluvia.data.CheckRepository // Import CheckRepository yang sudah Anda buat
import kotlinx.coroutines.launch
import android.util.Log // Gunakan Logcat untuk debugging
import com.mrifdnp.gluvia.data.DailyAverage
import com.mrifdnp.gluvia.data.MonthlyAverage // 🔑 Import data class baru


class CheckViewModel(
    private val checkRepository: CheckRepository // 🔑 Dependency HANYA CheckRepository
) : ViewModel() {

    // 🔑 STATE untuk Hasil di UI (diambil oleh CheckScreen)
    var calculatedGlukosa by mutableStateOf<Float?>(null)
        private set
    var isCalculated by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // --- LOGIKA UTILITY ---

    private fun calculateGlukosa(ph: Float): Float {
        // Rumus: Glukosa = -40.7 × pH + 395
        return (-40.7f * ph) + 395f
    }

    // --- FUNGSI UTAMA (Dipanggil dari CheckScreen) ---

    fun onCalculateAndSave(phInput: String) {
        phInput.toFloatOrNull()?.let { ph ->
            val glucoseResult = calculateGlukosa(ph)

            // 1. Tentukan status
            val status = when {
                glucoseResult < 70f -> "Hipoglikemia"
                glucoseResult > 126f -> "Tinggi/Warning"
                else -> "Normal"
            }

            // 2. Buat objek data
            val record = CheckRecord(
                phValue = ph,
                glucoseMgdl = glucoseResult,
                statusResult = status
            )

            // 3. Panggil fungsi penyimpanan
            viewModelScope.launch {
                checkRepository.saveCheckHistory(record).onFailure { e ->
                    // Set error message jika penyimpanan gagal
                    errorMessage = "Gagal menyimpan riwayat: ${e.message}"
                    Log.e("CheckViewModel", "Gagal menyimpan riwayat: ${e.message}")
                }
            }

            // 4. Update UI State
            calculatedGlukosa = glucoseResult
            isCalculated = true
            errorMessage = null
        }
    }
    var monthlyChartData by mutableStateOf<List<MonthlyAverage>>(emptyList())
        private set

    // 🔑 STATE BARU untuk Data Chart Harian
    var dailyChartData by mutableStateOf<List<DailyAverage>>(emptyList())
        private set

    // 🔑 STATE BARU untuk menentukan apakah data sudah dimuat
    var isLoadingChartData by mutableStateOf(true)
        private set

    init {
        // Panggil fungsi untuk memuat data historis saat ViewModel dibuat
        loadChartData()
    }

    // 🔑 FUNGSI BARU: Memuat data historis untuk grafik
    private fun loadChartData() {
        viewModelScope.launch {
            isLoadingChartData = true // Mulai loading

            // Muat Data Bulanan
            checkRepository.getMonthlyAverageGlucose().onSuccess { data ->
                monthlyChartData = data
            }.onFailure { e ->
                Log.e("CheckViewModel", "Gagal memuat data bulanan: ${e.message}")
            }

            // 🔑 Muat Data Harian
            checkRepository.getDailyAverageGlucose().onSuccess { data ->
                dailyChartData = data
            }.onFailure { e ->
                Log.e("CheckViewModel", "Gagal memuat data harian: ${e.message}")
            }

            isLoadingChartData = false // Loading selesai

            // 🔑 (Opsional) Tambahkan kembali logika logcat di sini jika diperlukan
        }
    }

}