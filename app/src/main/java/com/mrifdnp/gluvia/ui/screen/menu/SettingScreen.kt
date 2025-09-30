package com.mrifdnp.gluvia.ui.screen.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrifdnp.gluvia.ui.screen.GluviaHeader
import com.mrifdnp.gluvia.ui.screen.WaveShapeBackground
import com.mrifdnp.gluvia.ui.screen.home.AuthDarkGreen
import com.mrifdnp.gluvia.ui.screen.home.EduGreen
import com.mrifdnp.gluvia.ui.screen.home.White
import com.mrifdnp.gluvia.ui.viewmodel.ProfileViewModel
import org.koin.androidx.compose.koinViewModel



@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onNavigateToProfile: () -> Unit,
    profileViewModel: ProfileViewModel = koinViewModel(),
) {
    var selectedTab by remember { mutableStateOf("Akun Saya") }

    Scaffold(
        topBar = {
            GluviaHeader(
                onMenuClick = onBackClick,
                showTitle = true,
                showLogo = false,

            )
        },
        containerColor = EduGreen
    ) { paddingValues ->
        WaveShapeBackground(color = AuthDarkGreen, waveColor = EduGreen)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AuthDarkGreen) // Background di bawah header
        ) {
            // --- 1. TAB BAR ---
            SettingsTabBar(selectedTab) { selectedTab = it }

            // --- 2. KONTEN UTAMA ---
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(EduGreen) // 🔑 Area konten putih diganti EduGreen
                    .verticalScroll(rememberScrollState())
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    when (selectedTab) {
                        "Akun Saya" -> AccountContent(
                            profileViewModel = profileViewModel,
                            onUpdateInfoClick = onNavigateToProfile
                        )
                        "Pengaturan" -> UnderConstructionContent()
                    }

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

// --- AKUN SAYA: KONTEN UTAMA (Lihat Data) ---
@Composable
fun AccountContent(
    profileViewModel: ProfileViewModel,
    onUpdateInfoClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        // 1. JUDUL AKUN
        Text(
            text = "Akun",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = White, // 🔑 DIUBAH MENJADI PUTIH
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Lihat dan edit info pribadi anda di bawah ini",
            fontSize = 16.sp,
            color = White.copy(alpha = 0.8f), // 🔑 DIUBAH MENJADI PUTIH (Transparan)
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // 2. TOMBOL UTAMA
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tombol HAPUS
            ActionButtonStyle(
                text = "Hapus",
                isDestructive = true,
                isLoading = false,
                onClick = { /* Tampilkan konfirmasi dialog delete */ }
            )
            // Tombol Update Info
            ActionButtonStyle(
                text = "Update Info",
                isDestructive = false,
                isLoading = profileViewModel.isLoading,
                onClick = onUpdateInfoClick
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 3. DISPLAY INFO
        Text(
            text = "Display info",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = White, // 🔑 DIUBAH MENJADI PUTIH
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Informasi ini akan terlihat oleh semua pengguna situs ini",
            fontSize = 14.sp,
            color = White.copy(alpha = 0.8f), // 🔑 DIUBAH MENJADI PUTIH (Transparan)
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 🔑 INPUT 1: Nama Display (Username dari ProfileViewModel)
        OutlinedTextField(
            value = profileViewModel.username,
            onValueChange = { /* Read-only */ },
            label = { Text("Nama Display", color = White.copy(alpha = 0.8f)) }, // 🔑 Label Putih
            readOnly = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = White, // Border Putih saat fokus
                unfocusedBorderColor = White.copy(alpha = 0.5f), // Border Putih saat tidak fokus
                unfocusedTextColor = White, // Teks input Putih
                focusedTextColor = White
            )
        )

        // 🔑 INPUT 2: Title (Deskripsi/Title dari ProfileViewModel)
        OutlinedTextField(
            value = profileViewModel.description ?: "",
            onValueChange = { /* Read-only */ },
            label = { Text("Title", color = White.copy(alpha = 0.8f)) }, // 🔑 Label Putih
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = White,
                unfocusedBorderColor = White.copy(alpha = 0.5f),
                unfocusedTextColor = White,
                focusedTextColor = White
            )
        )
    }
}

// --- PENGATURAN: UNDER CONSTRUCTION ---
@Composable
fun UnderConstructionContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .height(200.dp)
            .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "PENGATURAN",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = White, // 🔑 DIUBAH MENJADI PUTIH
            textAlign = TextAlign.Center
        )
        Text(
            text = "Under Construction",
            fontSize = 18.sp,
            color = White.copy(alpha = 0.8f), // 🔑 DIUBAH MENJADI PUTIH
            textAlign = TextAlign.Center
        )
    }
}

// -----------------------------------------------------------------------------------
// --- KOMPONEN LAIN (SettingsTabBar, ActionButtonStyle, dll. tetap sama) ---
// -----------------------------------------------------------------------------------

@Composable
fun SettingsTabBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AuthDarkGreen) // Warna Tab Bar Kontras
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TabItem(
            title = "Akun Saya",
            isSelected = selectedTab == "Akun Saya",
            onClick = { onTabSelected("Akun Saya") }
        )
        TabItem(
            title = "Pengaturan",
            isSelected = selectedTab == "Pengaturan",
            onClick = { onTabSelected("Pengaturan") }
        )
    }
}

@Composable
fun RowScope.TabItem(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = White,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 18.sp
        )
        // Garis bawah untuk tab yang dipilih
        Divider(
            color = if (isSelected) White else Color.Transparent,
            thickness = 3.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )
    }
}

@Composable
fun RowScope.ActionButtonStyle(
    text: String,
    isDestructive: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDestructive) Color.Red.copy(alpha = 0.8f) else AuthDarkGreen,
            contentColor = White
        ),
        border = BorderStroke(1.dp, if (isDestructive) Color.Red else AuthDarkGreen),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.weight(1f).height(48.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
        } else {
            Text(text)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(onBackClick = {}, onNavigateToProfile = {})
}