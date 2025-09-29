package com.mrifdnp.gluvia.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrifdnp.gluvia.ui.screen.AuthDarkGreen
import com.mrifdnp.gluvia.ui.screen.GluviaHeader
import com.mrifdnp.gluvia.ui.screen.White
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import com.mrifdnp.gluvia.R
import com.mrifdnp.gluvia.ui.screen.DarkGreen
import com.mrifdnp.gluvia.ui.screen.WaveShapeBackground

// Ambil warna yang sudah didefinisikan sebelumnya (diselaraskan dengan ProfileScreen)
val EduGreen = Color(0xFF068b6b)
// AuthDarkGreen dan White sudah tersedia
val HeadGreen = Color(0xFF05ab83
)
@Composable
fun EduScreen(
    onBackClick: () -> Unit,
) {
    var isVideoMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            GluviaHeader(onMenuClick = onBackClick, showTitle = false, backgroundColor = EduGreen)
        },
        containerColor = HeadGreen // 🔑 Background Scaffold menggunakan SecondGreen (EduGreen)
    ) { paddingValues ->

        val layoutDirection = LocalLayoutDirection.current
        val columnPadding = Modifier.padding(
            start = paddingValues.calculateStartPadding(layoutDirection),
            top = paddingValues.calculateTopPadding(),
            end = paddingValues.calculateEndPadding(layoutDirection),
            bottom = 0.dp
        )

        // 🔑 CONTAINER UTAMA BERTUMPUK (SEPERTI PROFILE SCREEN)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(columnPadding)
        ) {
            // 1. Box Atas untuk Warna Header/Avatar (dari ProfileScreen)
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



            // 2. GAMBAR LATAR BELAKANG DENGAN OPACITY RENDAH (Ditengahkan)
            Image(
                painter = painterResource(id = R.drawable.edu),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.Center)
                    .alpha(0.5f),
                contentScale = ContentScale.Fit
            )

            // 3. WAVE FOOTER (Sama seperti ProfileScreen)
            WaveShapeBackground(
                color = AuthDarkGreen, // Warna dasar dari box parent
                waveColor = HeadGreen, // Warna gelombang
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .align(Alignment.BottomCenter)
            )

            // 4. KONTEN UTAMA (Yang bisa di-scroll)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, )
            ) {
                // 🔑 LOGIKA PENGGANTIAN KONTEN
                if (isVideoMode) {
                    VideoModeContent(onBackToMenu = onBackClick)
                } else {
                    TextModeContent(
                        onWatchVideoClick = { isVideoMode = true }
                    )
                }
                // Spacer untuk memastikan scroll menjangkau seluruh Wave Footer
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

// --- KOMPONEN MODE TEKS ---

@Composable
private fun TextModeContent(onWatchVideoClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        EduScreenTitle()
        // 1. Judul Utama
        Text(
            text = "Diabetes Melitus",
            color = White,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp, top = 20.dp),
            textAlign = TextAlign.Center,

        )

        // 2. Paragraf 1 (Definisi dan Komplikasi)
        Text(
            text = "Diabetes melitus adalah penyakit kronis akibat gangguan produksi atau kerja insulin, sehingga kadar gula darah meningkat. Jika tidak ditangani, kondisi ini dapat menyebabkan komplikasi serius seperti gangguan jantung dan kerusakan saraf.",
            color = White,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // 3. Paragraf 2 (Faktor Risiko dan Call to Action)
        Text(
            text = "Faktor risiko seperti pola makan tidak sehat, kurang aktivitas fisik, dan faktor genetik berperan besar dalam perkembangan diabetes. Untuk mengetahui lebih lanjut tentang definisi, gejala hingga cara pencegahan dan pengelolaannya, klik Lihat Video Edukasi sekarang dan dapatkan informasi penting untuk menjaga kesehatan anda. Berikut video edukasi mengenai diabetes melitus",
            color = White,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(bottom = 40.dp) // 🔑 Spacer di sini cukup
        )

        // 4. Tombol Aksi (Lihat Video Edukasi)
        Button(
            onClick = onWatchVideoClick, // Mengubah state di EduScreen
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGreen,
                contentColor = com.mrifdnp.gluvia.ui.screen.home.White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
            // 🔑 PERBAIKAN: Hapus padding bottom yang berlebihan di sini
        ) {
            Text("Lihat Video Edukasi", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }

        // 🔑 HAPUS SPACER BERLEBIHAN di akhir: Spacer(modifier = Modifier.height(40.dp))
    }
}
// --- KOMPONEN MODE VIDEO ---
@Composable
private fun EduScreenTitle() {
    Text(
        text = "Gluvi-Edu",
        color = White,
        fontSize = 40.sp,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
    )
}
@Composable
private fun VideoModeContent(onBackToMenu: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EduScreenTitle()
        Text(text = "Video Edukasi", color = White, fontSize = 26.sp,  modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp))

        Spacer(modifier = Modifier.height(24.dp))
        Box(modifier = Modifier.fillMaxWidth().height(220.dp).background(Color.Gray.copy(alpha = 0.5f)), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f))) {
                Icon(painter = painterResource(id = R.drawable.ic_google), contentDescription = "Play Video", tint = White, modifier = Modifier.size(64.dp))
            }
        }
        Column(modifier = Modifier.fillMaxWidth().padding(top = 40.dp, bottom = 40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = onBackToMenu, colors = ButtonDefaults.buttonColors(containerColor = AuthDarkGreen, contentColor = White), shape = RoundedCornerShape(50.dp), modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)) {
                Text("Kembali ke Main Menu", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EduScreenPreview() {
    EduScreen(
        onBackClick = {}
    )
}