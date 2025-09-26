package com.mrifdnp.gluvia.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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

// Ambil warna yang sudah didefinisikan sebelumnya (AuthDarkGreen, White)
val EduGreen = Color(0xFF068b6b) // Warna hijau yang sedikit berbeda untuk konten

@Composable
fun EduScreen(
    onBackClick: () -> Unit,
    onWatchVideoClick: () -> Unit
) {
    Scaffold(
        topBar = {
            // Re-use GluviaHeader. Kita bisa menggunakan onBackClick untuk tombol menu, atau
            // membuat header khusus dengan tombol back. Untuk sederhana, kita pakai tombol Menu
            GluviaHeader(onMenuClick = onBackClick, showTitle = false)
        },
        containerColor = AuthDarkGreen // Warna background hijau penuh sesuai mockup
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            // 1. Judul Utama
            Text(
                text = "Diabetes Melitus",
                color = White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
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
                modifier = Modifier.padding(bottom = 40.dp)
            )

            // 4. Tombol Aksi (Lihat Video Edukasi)
            Button(
                onClick = onWatchVideoClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor = AuthDarkGreen
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(bottom = 24.dp)
            ) {
                Text("Lihat Video Edukasi", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }

            // Spacer untuk memberi jarak dari bawah
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// ---

@Preview(showBackground = true)
@Composable
fun EduScreenPreview() {
    EduScreen(
        onBackClick = {},
        onWatchVideoClick = {}
    )
}