package com.mrifdnp.gluvia



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
// Import tambahan yang diperlukan untuk rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.mrifdnp.gluvia.ui.screen.GluviaScreen
import com.mrifdnp.gluvia.ui.screen.HomeScreen
import com.mrifdnp.gluvia.ui.screen.home.CareScreen
import com.mrifdnp.gluvia.ui.screen.home.CheckScreen
import com.mrifdnp.gluvia.ui.screen.home.EduScreen
import com.mrifdnp.gluvia.ui.screen.home.TrackScreen
import com.mrifdnp.gluvia.ui.theme.GluviaTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mrifdnp.gluvia.ui.screen.menu.ProfileScreen
import com.mrifdnp.gluvia.ui.screen.AuthScreen
import com.mrifdnp.gluvia.ui.screen.menu.SettingsScreen
import com.mrifdnp.gluvia.ui.viewmodel.AppState
import com.mrifdnp.gluvia.ui.viewmodel.MainViewModel
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GluviaTheme {
                MainNavigation()
            }
        }
    }
}
const val SCREEN_ONBOARDING = "onboarding"
const val SCREEN_AUTH = "auth"
const val SCREEN_HOME = "home"
const val SCREEN_EDU = "edu"
const val SCREEN_CHECK = "check"
const val SCREEN_TRACK = "track"
const val SCREEN_CARE = "care"
const val SCREEN_PROFILE = "profile_route"
const val SCREEN_SETTING = "settings_route"

@Composable
fun MainNavigation(mainViewModel: MainViewModel = koinViewModel()) { // 🔑 Inject MainViewModel

    // 🔑 Ambil state otentikasi
    val appState = mainViewModel.appState

    // 🔑 Tentukan rute awal dinamis (nullable)
    val startDestination: String? = when(appState) {
        AppState.ONBOARDING_REQUIRED -> SCREEN_ONBOARDING // 🔑 Rute awal baru
        AppState.UNAUTHENTICATED -> SCREEN_AUTH
        AppState.AUTHENTICATED -> SCREEN_HOME
        null -> null // Kasus ini akan digunakan saat state masih dimuat (Loading)
    }
    if (startDestination == null) {
        // Tampilkan Loading Spinner atau Splash Screen Anda di sini
        // Contoh:
        // LoadingScreen()
        return // Hentikan Composisi hingga startDestination ditentukan
    }

    // 1. Inisialisasi NavController
    val navController = rememberNavController()

    // 2. Definisi Callback yang menggunakan NavController

    // Callback umum untuk berpindah ke rute fitur
    val onFeatureClick: (route: String) -> Unit = { route ->
        // Navigasi sederhana ke rute tujuan
        navController.navigate(route)
    }

    // Onboarding Selesai -> Pergi ke Auth Screen
    val onOnboardFinish: () -> Unit = {
        mainViewModel.onOnboardingCompleted() // 🔑 Panggil fungsi di ViewModel untuk update state

        navController.navigate(SCREEN_AUTH) {
            popUpTo(SCREEN_ONBOARDING) { inclusive = true }
        }
    }
    // Pendaftaran Selesai -> Pergi ke Home Screen
    val onAuthSuccess: () -> Unit = {
        navController.navigate(SCREEN_HOME) {
            // Hapus Auth dari back stack (mencegah kembali ke Auth setelah login)
            popUpTo(SCREEN_AUTH) { inclusive = true }
        }
    }

    // Logout -> Pergi ke Auth Screen
    val onLogout: () -> Unit = {
        navController.navigate(SCREEN_AUTH) {
            // Hapus semua layar di back stack di atas Auth
            popUpTo(SCREEN_AUTH) { inclusive = true }
        }
    }

    // Handler untuk Tombol Kembali di Sub-Layar
    // Ini akan kembali ke layar sebelumnya (Edu -> Home, Check -> Edu, dll.)
    val onBack: () -> Unit = {
        navController.popBackStack()
    }


    // --- KONTROL NAVIGASI MENGGUNAKAN NavHost ---
    NavHost(
        navController = navController,
        // Ganti ke SCREEN_AUTH atau SCREEN_ONBOARDING untuk alur awal yang benar
        startDestination = startDestination
    ) {

        // 1. SCREEN_ONBOARDING
        composable(SCREEN_ONBOARDING) {
            GluviaScreen(onNextClick = onOnboardFinish) // Menggunakan onOnboardFinish
        }

        // 2. SCREEN_AUTH
        composable(SCREEN_AUTH) {
            AuthScreen(onNavigateToHome = onAuthSuccess)
        }

        // 3. SCREEN_HOME
        composable(SCREEN_HOME) {
            HomeScreen(onLogout = onLogout, onFeatureClick = onFeatureClick)
        }

        // 4. SCREEN_EDU
        composable(SCREEN_EDU) {
            EduScreen(
                onBackClick = onBack, // Kembali ke layar sebelumnya (Home)
            )
        }

        // 5. SCREEN_CHECK
        composable(SCREEN_CHECK) {
            CheckScreen(
                onBackToHome = onBack, // Kembali ke layar sebelumnya (Home)
                onNavigateToCare = { navController.navigate(SCREEN_CARE) } // Navigasi ke Care
            )
        }

        // 6. SCREEN_TRACK
        composable(SCREEN_TRACK) {
            TrackScreen(
                onBackToHome = onBack // Kembali ke layar sebelumnya (Home)
            )
        }

        // 7. SCREEN_CARE
        composable(SCREEN_CARE) {
            CareScreen(
                onBackToHome = onBack, // Kembali ke layar sebelumnya (Home atau Check)
                onCountySelected = { county ->
                    println("Mencari fasilitas di: $county")
                    // Contoh: Navigasi ke halaman hasil dengan argumen
                    // navController.navigate("faskes_result/$county")
                }
            )
        }
        composable(SCREEN_PROFILE) {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onEditProfileClick = { /* Navigasi ke Edit Screen */ }
            )
        }
        composable(SCREEN_SETTING) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToProfile = { navController.navigate(SCREEN_PROFILE) }
            )
        }
    }
}
