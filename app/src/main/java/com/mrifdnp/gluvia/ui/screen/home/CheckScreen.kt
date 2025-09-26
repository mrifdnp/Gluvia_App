package com.mrifdnp.gluvia.ui.screen.home

// --- Pastikan Anda sudah mengimpor Composable ini dari file lain ---
// import GluviaHeader
// import WaveShapeBackground (menggantikan AuthFooter)
// -----------------------------------------------------------------

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrifdnp.gluvia.R // Pastikan R diimpor
import com.mrifdnp.gluvia.ui.screen.GluviaHeader

// Warna yang digunakan (AuthDarkGreen, White, Black) sudah didefinisikan
val AuthDarkGreen = Color(0xFF016d54)
val White = Color(0xFFFFFFFF)
val RedWarning = Color(0xFFFF4444) // Warna merah untuk peringatan

@Composable
fun CheckScreen(
    onBackToHome: () -> Unit,
    onNavigateToCare: () -> Unit,
    // Di aplikasi nyata, Anda akan meneruskan data hasil dari ViewModel di sini
    glucoseValue: String = "85", // Placeholder
    isNormal: Boolean = true
) {
    Scaffold(
        topBar = {
            // Re-use GluviaHeader. Gunakan onBackToHome untuk tombol menu/kembali
            GluviaHeader(onMenuClick = onBackToHome, showTitle = false)
        },
        containerColor = AuthDarkGreen // Warna background hijau penuh
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            // 1. Judul Layar
            Text(
                text = "Gluvi-Check",
                color = White,
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )

            // 2. Area Ilustrasi dan Hasil Perkiraan
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp) // Tinggi tetap untuk menampung ilustrasi
            ) {
                // Ilustrasi Utama
                Image(
                    painter = painterResource(id = R.drawable.check), // Ganti dengan drawable yang benar
                    contentDescription = "Ilustrasi Pengecekan Gula Darah",
                    modifier = Modifier.fillMaxSize(0.8f)
                )

                // Kotak Hasil Perkiraan Kadar Gula Darah
                ResultBox(glucoseValue = glucoseValue)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Catatan/Notes
            NotesSection(isNormal = isNormal)

            Spacer(modifier = Modifier.height(32.dp))

            // 4. Tombol Aksi (Lihat Fasilitas Kesehatan Terdekat)
            CheckActionButton(
                text = "Lihat Fasilitas Kesehatan Terdekat",
                onClick = onNavigateToCare,
                isPrimary = true
            )

            // 5. Tombol Kembali ke Home
            CheckActionButton(
                text = "Kembali ke Home",
                onClick = onBackToHome,
                isPrimary = false
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// --- Komponen Pembantu ---

@Composable
fun ResultBox(glucoseValue: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(50.dp)
            .background(White, shape = RoundedCornerShape(25.dp))
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Hasil Perkiraan Kadar Gula Darah",
            color = AuthDarkGreen,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun NotesSection(isNormal: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Catatan/notes",
            color = if (isNormal) White else RedWarning,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Nilai normal : 70 – 126 mg/dL",
            color = White,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = if (isNormal) {
                "Nilai Anda dalam batas normal."
            } else {
                "Jika diluar nilai normal, sebaiknya merujuk ke fasilitas kesehatan terdekat"
            },
            color = if (isNormal) White else RedWarning,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CheckActionButton(
    text: String,
    onClick: () -> Unit,
    isPrimary: Boolean
) {
    val backgroundColor = if (isPrimary) White else AuthDarkGreen
    val textColor = if (isPrimary) AuthDarkGreen else White
    val borderColor = if (isPrimary) Color.Transparent else White

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        shape = RoundedCornerShape(50.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(50.dp)
            .padding(bottom = 12.dp)
    ) {
        Text(text, fontWeight = FontWeight.SemiBold)
    }
}

// --- Preview ---

@Preview(showBackground = true)
@Composable
fun CheckScreenPreview() {
    CheckScreen(
        onBackToHome = {},
        onNavigateToCare = {},
        glucoseValue = "85",
        isNormal = true
    )
}

@Preview(showBackground = true)
@Composable
fun CheckScreenWarningPreview() {
    CheckScreen(
        onBackToHome = {},
        onNavigateToCare = {},
        glucoseValue = "150",
        isNormal = false // Mode peringatan
    )
}