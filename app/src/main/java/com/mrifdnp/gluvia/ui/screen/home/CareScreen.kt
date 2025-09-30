package com.mrifdnp.gluvia.ui.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrifdnp.gluvia.R
import com.mrifdnp.gluvia.ui.screen.GluviaHeader
import com.mrifdnp.gluvia.ui.screen.WaveShapeBackground
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import com.mrifdnp.gluvia.ui.screen.AuthDarkGreen
import com.mrifdnp.gluvia.ui.screen.LinkColor
// 🔑 Import Data
import com.mrifdnp.gluvia.data.Hospital // Import data class Hospital
import com.mrifdnp.gluvia.data.getHospitalsForCounty // Import fungsi data
import com.mrifdnp.gluvia.ui.screen.White // Asumsi Warna White diimpor

// Warna yang digunakan (Diselaraskan dengan CheckScreen/ProfileScreen)

@Composable
fun CareScreen(
    onBackToHome: () -> Unit,
    onCountySelected: (county: String) -> Unit = {} // Parameter ini tidak digunakan di sini lagi
) {

    var selectedCounty by remember { mutableStateOf<String?>(null) }

    // 🔑 Handler untuk berpindah ke tampilan daftar rumah sakit
    val onCountyClicked: (county: String) -> Unit = { county ->
        selectedCounty = county
    }

    Scaffold(
        topBar = {
            GluviaHeader(onMenuClick = onBackToHome, showTitle = false, backgroundColor = SecondGreen)
        },
        containerColor = HeadGreen
    ) { paddingValues ->

        val layoutDirection = LocalLayoutDirection.current
        val contentPadding = Modifier.padding(
            start = paddingValues.calculateStartPadding(layoutDirection),
            top = paddingValues.calculateTopPadding(),
            end = paddingValues.calculateEndPadding(layoutDirection),
            bottom = 0.dp
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(contentPadding)
        ) {

            // 1. Box Atas untuk Warna Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(color = AuthDarkGreen)
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(color = SecondGreen )
                )
            }

            // 2. GAMBAR LATAR BELAKANG DENGAN OPACITY RENDAH
            Image(
                painter = painterResource(id = R.drawable.care), // Asumsi R.drawable.care ada
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.Center)
                    .alpha(0.5f),
                contentScale = ContentScale.Fit
            )

            // 3. WAVE FOOTER
            WaveShapeBackground(
                color = AuthDarkGreen,
                waveColor = HeadGreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .align(Alignment.BottomCenter)
            )

            // 4. KONTEN UTAMA
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Spacer untuk mengimbangi Box Header/TopBar
                Spacer(modifier = Modifier.height(32.dp))

                // 1. Judul Layar
                CareHeader()

                // 🔑 LOGIC PERGANTIAN VIEW
                when (val county = selectedCounty) {
                    null -> {
                        // --- VIEW 1: PILIH KABUPATEN ---
                        Text(text = "KABUPATEN", color = White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(top = 32.dp, bottom = 24.dp))
                        CountyGrid(onCountySelected = onCountyClicked)
                        Spacer(modifier = Modifier.height(60.dp))
                    }
                    else -> {
                        // --- VIEW 2: DAFTAR FASILITAS ---
                        HospitalListContent(
                            countyName = county,
                            hospitalList = getHospitalsForCounty(county),
                            onBackToList = { selectedCounty = null }, // Kembali ke grid kabupaten
                            onBackToHome = onBackToHome
                        )
                    }
                }
            }
        }
    }
}

// --- Komponen Pembantu ---

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
    onBackToList: () -> Unit, // Handler baru untuk kembali ke daftar kabupaten
    onBackToHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Tombol Kembali ke Daftar Kabupaten
        TextButton(onClick = onBackToList) {
            Text("← Kembali ke Daftar Kabupaten", color = White)
        }

        // Judul Fasilitas
        Text(
            text = "FASILITAS KESEHATAN",
            color = White,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 16.dp)
        )
        // Nama Kabupaten
        Text(
            text = countyName,
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Daftar Kartu
        if (hospitalList.isEmpty()) {
            Text("Tidak ada data fasilitas kesehatan yang tersedia.", color = White, textAlign = TextAlign.Center)
        } else {
            hospitalList.forEach { hospital ->
                HospitalCard(hospital = hospital)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Tombol Kembali ke Main Menu
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


@Composable
fun HospitalCard(hospital: Hospital) {
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
            // Gambar Rumah Sakit Placeholder
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
                Text(text = hospital.name, color = White, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📞", color = LinkColor, modifier = Modifier.size(16.dp)) // Placeholder Icon
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = hospital.phone, color = White, fontSize = 14.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📍", color = LinkColor, modifier = Modifier.size(16.dp)) // Placeholder Icon
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
        Text(county, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, textAlign = TextAlign.Center)
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