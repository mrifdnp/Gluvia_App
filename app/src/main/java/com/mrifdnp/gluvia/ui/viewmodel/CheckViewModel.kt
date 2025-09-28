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

    init {
        // Panggil fungsi untuk memuat data historis saat ViewModel dibuat
        loadChartData()
    }

    // 🔑 FUNGSI BARU: Memuat data historis untuk grafik
    private fun loadChartData() {
        viewModelScope.launch {
            checkRepository.getMonthlyAverageGlucose().onSuccess { data ->
                monthlyChartData = data // Perbarui state untuk grafik

                // 🔑 LOGIK LOGCAT BARU DIMULAI DI SINI
                Log.i("GLUVI_TRACK_LOG", "--- LOG RIWAYAT GLUKOSA BULANAN ---")
                if (data.isEmpty()) {
                    Log.i("GLUVI_TRACK_LOG", "Data riwayat glukosa kosong.")
                } else {
                    data.forEach { monthlyAvg ->
                        Log.i("GLUVI_TRACK_LOG", "Bulan: ${monthlyAvg.monthLabel} | Rata-Rata Glukosa: ${String.format("%.2f", monthlyAvg.averageGlucose)} mg/dL")
                    }
                }
                Log.i("GLUVI_TRACK_LOG", "--- LOG SELESAI ---")
                // 🔑 LOGIK LOGCAT BARU SELESAI DI SINI

            }.onFailure { e ->
                Log.e("CheckViewModel", "Gagal memuat data grafik: ${e.message}")
            }
        }
    }

}