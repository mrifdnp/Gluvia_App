package com.mrifdnp.gluvia.ui.screen

import com.mrifdnp.gluvia.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
// Import yang diperlukan untuk menangani Status Bar Insets
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection


 // Warna hijau untuk Header dan Area Deskripsi
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
 val DarkGreen = Color(0xFF016d54)
@Composable
fun GluviaScreen(onNextClick: () -> Unit) {
    Scaffold(
        // Header
        topBar = {
            GluviaHeader(
                onMenuClick = {
                    // Aksi saat tombol menu diklik
                }, showLogo = false
            )
        },
        // Scaffold secara otomatis menyediakan padding untuk topBar
        containerColor = White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                // PaddingValues dari Scaffold sudah mencakup tinggi TopBar dan Status Bar (karena TopBar sudah diperbaiki)

        ) {
            // Area Ilustrasi
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.gluvia),
                    contentDescription = "Ilustrasi Diabetes Melitus",
                    modifier = Modifier
                        .fillMaxSize()


                )

                // Teks "GLUVIA" di tengah, sedikit menjorok ke Area Deskripsi

            }

            // Area Deskripsi dan Tombol "Berikutnya"
            GluviaDescriptionArea(
                onNextClick = onNextClick
            )
        }
    }
}


@Composable
fun GluviaHeader(
    onMenuClick: () -> Unit,
    showTitle: Boolean = true, // <-- Kontrol visibilitas Title (Gluvia)
    showLogo: Boolean = true,  // <-- Kontrol visibilitas Logo
    logoResId: Int = R.drawable.logo_sma, // <-- Ganti dengan R.drawable.logo_sma atau logo yang sesuai
    backgroundColor: Color = DarkGreen, // 🔑 Warna latar belakang header (default DarkGreen)

) {
    // Gunakan Column untuk menumpuk Status Bar Spacer dan Row Header
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
    ) {
        // PERBAIKAN: Spacer ini memberikan tinggi yang dibutuhkan untuk Status Bar
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
        )

        // Konten utama Header (Logo, Teks, dan Ikon Menu)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 1. KIRI: Title/Text (Mengambil 1f ruang)
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                if (showTitle) {
                    Text(
                        text = "Gluvia",
                        color = White,
                        fontSize = 24.sp,
                    )
                }
            }


            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                if (showLogo) {
                    Image(
                        painter = painterResource(id = logoResId),
                        contentDescription = "Logo Aplikasi",
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // 3. KANAN: Menu Icon (Mengambil 1f ruang)
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        tint = White
                    )
                }
            }
        }
    }
}



@Composable
fun GluviaDescriptionArea(onNextClick: () -> Unit) {
    // ... (Fungsi ini tidak perlu diubah)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF016d54)) // Panggilan ke-1 (akan menggunakan bentuk persegi default)
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
            .background( // Panggilan ke-2, ini yang menerapkan shape
                color = Color(0xFF068b6b),
                shape = WaveShape()
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 70.dp, start = 32.dp, end = 32.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val descriptionText = "GLUVIA merupakan inovasi aplikasi berbasis website digunakan sebagai alternatif pendeteksi glukosa darah non-invasif dengan berdasarkan analisis dataset pH saliva mulut untuk penanganan penyakit Diabetes Melitus."
            Text(
                text = "GLUVIA",
                color = White,
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                modifier = Modifier
                        .fillMaxWidth(0.6f)
                    .height(50.dp)
            )
            Text(
                text = descriptionText,
                color = White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            Button(
                onClick = onNextClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor = Black
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth(0.6f)

                    .height(50.dp)
            ) {
                Text(
                    text = "Berikutnya",
                    fontSize = 18.sp,
                )
            }
        }
    }
}
class WaveShape : Shape {

    fun getPath(size: Size, density: Density): Path {

        val NORM_X = 15.0f
        val NORM_Y = 7.0f

        val curveHeight = size.height * 0.40f

        val yOffsetGlobal = size.height * 0.50f

        val path = Path().apply {

            // 1. Mulai dari kiri atas (0, 0) + offset
            moveTo(0f, 0f )

            // 2. Garis lurus ke kanan atas + offset
            lineTo(size.width, 0f)

            // 3. Pindah ke Titik Awal Kurva (P0) + offset
            val startX = size.width * (15f / NORM_X)
            val startY = curveHeight * (2f / NORM_Y)

            lineTo(startX, startY + yOffsetGlobal) // Tambahkan offset ke Y

            // MENGGAMBAR KURVA DARI KANAN KE KIRI
            cubicTo(
                // Kontrol 1: P1 (7.47, 0.04) + offset
                x1 = size.width * (7.47f / NORM_X),
                y1 = (curveHeight * (0.04f / NORM_Y)) + yOffsetGlobal, // Tambahkan offset ke Y

                // Kontrol 2: P2 (7.24, 6.04) + offset
                x2 = size.width * (7.24f / NORM_X),
                y2 = (curveHeight * (6.04f / NORM_Y)) + yOffsetGlobal, // Tambahkan offset ke Y

                // Titik Akhir: P3 (-0.13, 3.05) + offset
                x3 = size.width * (-0.13f / NORM_X),
                y3 = (curveHeight * (1.05f / NORM_Y)) + yOffsetGlobal // Tambahkan offset ke Y
            )

            // 5. Tutup jalur (kembali ke kiri atas 0,0) + offset
            lineTo(0f, 0f + yOffsetGlobal)
            close()
        }
        return path
    }

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        return Outline.Generic(getPath(size, density))
    }
}
// ... kode Anda sebelumnya ...

// --- Pratinjau Tambahan dengan Ukuran Manual ---

// Pratinjau Tablet Portrait (Lebar ~800dp)
@Preview(
    name = "Tablet Manual - Portrait",
    widthDp = 800,
    heightDp = 1280,
    showBackground = true
)
@Composable
fun PreviewGluviaTabletManualPortrait() {
    GluviaScreen(onNextClick = {})
}



// Pratinjau Tablet Landscape (Tinggi ~800dp - Menguji Scroll)
@Preview(
    name = "Tablet Manual - Landscape (Scroll Test)",
    widthDp = 1280,
    heightDp = 800,
    showBackground = true
)
@Composable
fun PreviewGluviaTabletManualLandscape() {
    GluviaScreen(onNextClick = {})
}

// --- Pratinjau Standar Anda Tetap Ada ---

@Preview(showBackground = true)
@Composable
fun PreviewGluviaScreen() {
    GluviaScreen(onNextClick = {})
}

@Preview(showBackground = true)
@Composable
fun PreviewWave() {
    WaveShape()
}