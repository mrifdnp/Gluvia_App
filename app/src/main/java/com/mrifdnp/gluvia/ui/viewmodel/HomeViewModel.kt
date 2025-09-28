// --- File: HomeViewModel.kt ---

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.mrifdnp.gluvia.R // Pastikan R diimpor
import com.mrifdnp.gluvia.data.AuthRepository
import kotlinx.coroutines.launch

@Immutable // Menandakan bahwa data class ini stabil, membantu Compose
data class FeatureCard(
    val title: String,
    val description: String, // Bisa digunakan untuk ContentDescription atau teks lebih lanjut
    val iconResId: Int,
    val route: String // Untuk navigasi
)

class HomeViewModel(private val authRepository: AuthRepository) : ViewModel() {
    val userName = "Pengguna" // Ganti dengan nama pengguna setelah login

    val featureCards = listOf(
        FeatureCard(
            title = "Gluvi-Edu",
            description = "Edukasi Diabetes",
            iconResId = R.drawable.edu, // Ganti dengan drawable yang sesuai
            route = "edu"
        ),
        FeatureCard(
            title = "Gluvi-Check",
            description = "Pencatatan Gula Darah",
            iconResId = R.drawable.check, // Ganti dengan drawable yang sesuai
            route = "check"
        ),
        FeatureCard(
            title = "Gluvi-Track",
            description = "Monitoring Data",
            iconResId = R.drawable.track, // Ganti dengan drawable yang sesuai
            route = "track"
        ),
        FeatureCard(
            title = "Gluvi-Care",
            description = "Informasi Fasilitas Kesehatan",
            iconResId = R.drawable.care, // Ganti dengan drawable yang sesuai
            route = "care"
        )
    )
    fun onLogoutClicked(onNavigateToAuth: () -> Unit) {
        viewModelScope.launch {
            // Walaupun Sign Out gagal di server, kita tetap anggap berhasil
            // di sisi client agar aplikasi bisa lanjut.
            val result = authRepository.signOut()

            if (result.isSuccess || result.isFailure) {
                // Selalu navigasi ke layar Auth setelah mencoba sign out,
                // karena sesi lokal sudah dihapus (atau diasumsikan invalid).
                onNavigateToAuth()
            }
        }
    }
}