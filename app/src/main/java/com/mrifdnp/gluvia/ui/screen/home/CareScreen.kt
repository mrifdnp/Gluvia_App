package com.mrifdnp.gluvia.ui.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrifdnp.gluvia.R // Pastikan R diimpor
import com.mrifdnp.gluvia.ui.screen.GluviaHeader
import com.mrifdnp.gluvia.ui.screen.WaveShapeBackground
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import com.mrifdnp.gluvia.ui.screen.AuthDarkGreen
import com.mrifdnp.gluvia.ui.screen.LinkColor


// Warna yang digunakan (Diselaraskan dengan CheckScreen/ProfileScreen)

@Composable
fun CareScreen(
    onBackToHome: () -> Unit,
    onCountySelected: (county: String) -> Unit
) {

    var selectedCounty by remember { mutableStateOf<String?>(null) }

    // 🔑 MODIFIKASI: Aksi saat county diklik (mengubah state)
    val onCountySelected: (county: String) -> Unit = { county ->
        selectedCounty = county
    }

    Scaffold(
        topBar = {
            // Kita set showTitle=true agar header terlihat sama dengan Check/ProfileScreen
            GluviaHeader(onMenuClick = onBackToHome, showTitle = false, backgroundColor = SecondGreen)
        },
        containerColor = HeadGreen // 🔑 Background dasar Scaffold menggunakan SecondGreen
    ) { paddingValues ->

        val layoutDirection = LocalLayoutDirection.current
        val contentPadding = Modifier.padding(
            start = paddingValues.calculateStartPadding(layoutDirection),
            top = paddingValues.calculateTopPadding(),
            end = paddingValues.calculateEndPadding(layoutDirection),
            bottom = 0.dp
        )

        // 🔑 CONTAINER UTAMA BERTUMPUK (BOX)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(contentPadding)
        ) {

            // 1. Box Atas untuk Warna Header (dari Check/ProfileScreen)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(color = AuthDarkGreen) // Warna hijau gelap di bagian atas

            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(color = SecondGreen ) // Warna hijau gelap di bagian atas

                )

            }

            // 🔑 PERUBAHAN DI SINI: Menggunakan R.drawable.edu dengan alpha 0.5f (50%)
            Image(
                painter = painterResource(id = R.drawable.care), // 🔑 MENGGUNAKAN EDU.PNG
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp) // Ukuran bisa disesuaikan
                    .align(Alignment.Center)
                    .alpha(0.5f), // 🔑 OPACITY 50%
                contentScale = ContentScale.Fit
            )

            // 3. WAVE FOOTER (Sama seperti ProfileScreen)
            WaveShapeBackground(
                color = AuthDarkGreen, // Warna dasar gelombang
                waveColor = HeadGreen, // Warna yang muncul di bawah gelombang
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .align(Alignment.BottomCenter)
            )

            // 4. KONTEN UTAMA (Di atas semua latar belakang)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Sisa konten CareScreen

                // Spacer untuk mengimbangi Box Header/TopBar
                Spacer(modifier = Modifier.height(32.dp))

                // 1. Judul Layar
                CareHeader()
                when (val county = selectedCounty) {
                    null -> {
                        // --- VIEW 1: PILIH KABUPATEN ---
                        Text(text = "KABUPATEN", color = White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(top = 32.dp, bottom = 24.dp))
                        CountyGrid(onCountySelected = onCountySelected)
                        Spacer(modifier = Modifier.height(60.dp))
                    }
                    else -> {
                        // --- VIEW 2: DAFTAR FASILITAS ---
                        HospitalListContent(
                            countyName = county,
                            hospitalList = getHospitalsForCounty(county),
                            onBackToHome = onBackToHome
                        )
                    }
                }

                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

// --- Komponen Pembantu (Dipertahankan) ---

@Composable
fun CareHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SecondGreen)
            .padding(horizontal = 24.dp),
    ) {
        Text(
            text = "Gluvi-Care",
            color = White,
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(16.dp).background(Color.Black))
    }
}

@Composable
fun CountyGrid(onCountySelected: (county: String) -> Unit) {

    val counties = listOf(
        "BADUNG", "DENPASAR",
        "GIANYAR", "TABANAN",
        "BANGLI", "KARANGASEM",
        "BULELENG", "KLUNGKUNG"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        counties.chunked(2).forEach { rowCounties ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowCounties.forEach { county ->
                    CountyButton(
                        county = county,
                        onClick = { onCountySelected(county) },
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        CountyButton(
            county = "JEMBRANA",
            onClick = { onCountySelected("JEMBRANA") },
            modifier = Modifier.fillMaxWidth(0.6f)
        )
    }
}


@Composable
fun HospitalListContent(
    countyName: String,
    hospitalList: List<Hospital>,
    onBackToHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp), // Padding untuk selaraskan dengan header
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Judul Fasilitas (Sesuai Mockup)
        Text(
            text = "FASILITAS KESEHATAN",
            color = White,
            fontSize = 28.sp,

            modifier = Modifier.padding(top = 16.dp)
        )
        // Nama Kabupaten
        Text(
            text = countyName,
            color = White,
            fontSize = 18.sp,

            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Daftar Kartu
        hospitalList.forEach { hospital ->
            HospitalCard(hospital = hospital)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Tombol Kembali
        Button(
            onClick = onBackToHome,
            colors = ButtonDefaults.buttonColors(
                containerColor = White,
                contentColor = AuthDarkGreen
            ),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(50.dp)
                .padding(vertical = 16.dp)
        ) {
            Text("Kembali ke Main Menu", fontWeight = FontWeight.SemiBold)
        }
    }
}


// --- Komponen Pembantu (Dipertahankan) ---

@Composable
fun HospitalCard(hospital: Hospital) {
    // Gunakan Icons.Filled.Phone dan Icons.Filled.LocationOn
    // ... (Implementasi HospitalCard dari jawaban sebelumnya) ...
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AuthDarkGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = hospital.imageResId),
                contentDescription = hospital.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(White)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = hospital.name, color = White, fontSize = 18.sp, )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Ikon Phone Placeholder
                    Text("📞", color = LinkColor, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = hospital.phone, color = White, fontSize = 14.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Ikon Location Placeholder
                    Text("📍", color = LinkColor, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = hospital.address, color = White, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun CountyButton(county: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = AuthDarkGreen.copy(alpha = 0.7f),
            contentColor = White
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, White),
        modifier = modifier
            .height(50.dp)
    ) {
        Text(county, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
    }
}

// --- Preview ---

@Preview(showBackground = true)
@Composable
fun CareScreenPreview() {
    CareScreen(
        onBackToHome = {},
        onCountySelected = {}
    )
}