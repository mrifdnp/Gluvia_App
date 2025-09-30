package com.mrifdnp.gluvia



import HomeViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
// Import tambahan yang diperlukan untuk rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
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
import com.mrifdnp.gluvia.ui.screen.HomeDrawerContent
import com.mrifdnp.gluvia.ui.screen.home.SecondGreen
import com.mrifdnp.gluvia.ui.screen.menu.SettingsScreen
import com.mrifdnp.gluvia.ui.viewmodel.AppState
import com.mrifdnp.gluvia.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch
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
fun MainNavigation(mainViewModel: MainViewModel = koinViewModel(),homeViewModel: HomeViewModel = koinViewModel() ) { // 🔑 Inject MainViewModel

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

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 🔑 Fungsi untuk membuka Drawer
    val openDrawer: () -> Unit = { scope.launch { drawerState.open() } }
    // Fungsi untuk menutup Drawer
    val closeDrawer: () -> Unit = { scope.launch { drawerState.close() } }

    // 3. Definisi Callback Navigasi

    // Callback umum untuk berpindah ke rute fitur
    val onFeatureClick: (route: String) -> Unit = { route ->
        scope.launch { drawerState.close() } // Tutup drawer setelah navigasi
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

// 🔑 DEFINISI FUNGSI onDrawerNavigate (Ini yang hilang!)
    val onDrawerNavigate: (route: String) -> Unit = { route ->
        closeDrawer()
        when (route) {
            "logout_route" -> {
                homeViewModel.onLogoutClicked(onLogout) // Gunakan onLogout yang sudah didefinisikan
            }
            else -> {
                navController.navigate(route)
            }
        }
    }
    val thresholdPx = with(LocalDensity.current) { 50.dp.toPx() } // threshold 50dp

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true, // tetap aktif
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = SecondGreen,
                content = {
                    HomeDrawerContent(
                        featureRoutes = homeViewModel.featureCards.map { Pair(it.title, it.route) },
                        onCloseDrawer = { scope.launch { drawerState.close() } },
                        onNavigate = onDrawerNavigate // Sekarang onDrawerNavigate sudah terdefinisi!
                    )
                }
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { change, dragAmount ->
                            if (dragAmount > thresholdPx) { // swipe kanan lebih dari 50dp
                                scope.launch { drawerState.open() }
                            }
                        }
                    }
            ) {
            // --- KONTROL NAVIGASI MENGGUNAKAN NavHost ---
            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {

                // 1. SCREEN_ONBOARDING
                composable(SCREEN_ONBOARDING) {
                    GluviaScreen(onNextClick = onOnboardFinish)
                }

                // 2. SCREEN_AUTH
                composable(SCREEN_AUTH) {
                    AuthScreen(onNavigateToHome = onAuthSuccess)
                }

                // 3. SCREEN_HOME
                composable(SCREEN_HOME) {
                    HomeScreen(
                        onLogout = onLogout,
                        onFeatureClick = onFeatureClick,
                        // Agar HomeHeader dapat membuka drawer
                        onMenuClick = openDrawer
                    )
                }

                // 4. SCREEN_EDU
                composable(SCREEN_EDU) {
                    EduScreen(
                        // Menggunakan onBack (asumsi tombol header di Edu berfungsi kembali),
                        // ubah ke openDrawer jika tombol menu ingin membukanya.
                        onMenuClick = openDrawer,
                        onBackToHome = onBack
                    )
                }

                // 5. SCREEN_CHECK
                composable(SCREEN_CHECK) {
                    CheckScreen(
                        // Menggunakan openDrawer
                        onMenuClick = openDrawer,
                        onNavigateToCare = { navController.navigate(SCREEN_CARE) }
                    )
                }

                // 6. SCREEN_TRACK
                composable(SCREEN_TRACK) {
                    TrackScreen(
                        onMenuClick = openDrawer,
                        onBackToHome = onBack
                    )
                }

                // 7. SCREEN_CARE
                composable(SCREEN_CARE) {
                    CareScreen(
                        onMenuClick = openDrawer,
                        onCountySelected = { county -> println("Mencari fasilitas di: $county") },
                        onBackToHome = onBack
                    )
                }

                // 8. SCREEN_PROFILE (Layar Menu)
                composable(SCREEN_PROFILE) {
                    ProfileScreen(
                        onMenuClick = openDrawer,
                        onEditProfileClick = { /* Navigasi ke Edit Screen */ }
                    )
                }

                // 9. SCREEN_SETTING (Layar Menu)
                composable(SCREEN_SETTING) {
                    SettingsScreen(
                        onMenuClick = openDrawer,
                        onNavigateToProfile = { navController.navigate(SCREEN_PROFILE) }
                    )
                }
            }
        }

        }
    )
}
