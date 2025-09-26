package com.mrifdnp.gluvia.ui.screen.home


// --- Pastikan import dasar sudah ada di file ini ---
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrifdnp.gluvia.ui.screen.GluviaHeader

// ----------------------------------------------------


@Composable
fun CareScreen(
    onBackToHome: () -> Unit,
    onCountySelected: (county: String) -> Unit
) {
    Scaffold(
        topBar = {
            // Re-use GluviaHeader. Tombol menu/kembali berfungsi untuk kembali ke Home
            GluviaHeader(onMenuClick = onBackToHome)
        },
        containerColor = White
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AuthDarkGreen) // Background hijau penuh untuk seluruh konten
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 1. Judul Layar
            CareHeader()

            // 2. Judul Kabupaten
            Text(
                text = "KABUPATEN",
                color = White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 32.dp, bottom = 24.dp)
            )

            // 3. Grid Pilihan Kabupaten
            CountyGrid(onCountySelected = onCountySelected)

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

// --- Komponen Pembantu ---

@Composable
fun CareHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AuthDarkGreen)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Gluvi-Care",
            color = White,
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CountyGrid(onCountySelected: (county: String) -> Unit) {

    // Daftar Kabupaten Bali (Contoh dari mockup)
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
        // Baris demi Baris (2 kolom)
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

        // Tombol Jembrana (tombol tunggal di tengah)
        Spacer(modifier = Modifier.height(32.dp))
        CountyButton(
            county = "JEMBRANA",
            onClick = { onCountySelected("JEMBRANA") },
            modifier = Modifier.fillMaxWidth(0.6f)
        )
    }
}


@Composable
fun CountyButton(county: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = AuthDarkGreen.copy(alpha = 0.7f), // Warna hijau yang sedikit transparan
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