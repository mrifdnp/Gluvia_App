package com.mrifdnp.gluvia

import AuthScreen

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
@Composable
fun MainNavigation() {

    var currentScreen by rememberSaveable { mutableStateOf(SCREEN_HOME) }

    // --- CALLBACK HANDLERS ---
    val onFeatureClick: (route: String) -> Unit = { route ->
        currentScreen = route // <-- Mengubah layar ke rute yang diklik (misal: "edu")
    }
    // 1. Onboarding Selesai -> Pergi ke Auth Screen
    val onOnboardFinish: () -> Unit = {
        currentScreen = SCREEN_AUTH
    }

    // 2. Pendaftaran Selesai -> Pergi ke Home Screen
    val onAuthSuccess: () -> Unit = {
        currentScreen = SCREEN_HOME
    }

    // 3. Logout/Kembali -> Pergi ke Auth Screen
    val onLogout: () -> Unit = {
        currentScreen = SCREEN_AUTH
    }


    // --- KONTROL NAVIGASI ---
    when (currentScreen) {

        SCREEN_ONBOARDING -> {
            // Asumsi: GluviaScreen adalah layar Onboarding Anda
            GluviaScreen(onNextClick = onOnboardFinish)
        }

        SCREEN_AUTH -> {
            // Tampilkan Auth Screen
            AuthScreen(onNavigateToHome = onAuthSuccess)
        }

        SCREEN_HOME -> {
            // Tampilkan Home Screen
            // Berikan callback logout jika ada
            HomeScreen(onLogout = onLogout, onFeatureClick = onFeatureClick)
        }
        SCREEN_EDU -> {
            EduScreen(
                onBackClick = { currentScreen = SCREEN_HOME }, // Kembali ke Home
                onWatchVideoClick = { /* Aksi buka video */ }
            )
        }
        SCREEN_CHECK -> {
            CheckScreen(
                onBackToHome = { currentScreen = SCREEN_HOME }, // Kembali ke Home
                onNavigateToCare = { currentScreen = "care" } // Rute ke Gluvi-Care
            )
        }
        SCREEN_TRACK -> {
            TrackScreen(
                onBackToHome = { currentScreen = SCREEN_HOME } // Kembali ke Home
            )
        }
        SCREEN_CARE -> {
            CareScreen(
                onBackToHome = { currentScreen = SCREEN_HOME }, // Kembali ke Home
                onCountySelected = { county ->
                    // Logika untuk mencari Faskes berdasarkan kabupaten
                    println("Mencari fasilitas di: $county")
                    // currentScreen = SCREEN_FASKES_RESULT (Jika ada layar hasil)
                }
            )
        }
    }
}