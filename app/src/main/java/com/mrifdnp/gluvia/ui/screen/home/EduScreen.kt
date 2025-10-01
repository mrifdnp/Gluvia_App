package com.mrifdnp.gluvia.ui.screen.home

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.mrifdnp.gluvia.R
import com.mrifdnp.gluvia.ui.screen.*

@Composable
fun EduScreen(
    onMenuClick: () -> Unit,
    onBackToHome: () -> Unit
) {
    var isVideoMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            GluviaHeader(onMenuClick = onMenuClick, showTitle = false, backgroundColor = EduGreen)
        },
        containerColor = HeadGreen
    ) { paddingValues ->

        val layoutDirection = LocalLayoutDirection.current
        val paddingModifier = Modifier.padding(
            start = paddingValues.calculateStartPadding(layoutDirection),
            top = paddingValues.calculateTopPadding(),
            end = paddingValues.calculateEndPadding(layoutDirection),
            bottom = 0.dp
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(paddingModifier)
        ) {
            // Background Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(color = AuthDarkGreen)
            )

            // Background Image
            Image(
                painter = painterResource(id = R.drawable.edu),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.Center)
                    .alpha(0.5f),
                contentScale = ContentScale.Fit
            )

            // Wave Footer
            WaveShapeBackground(
                color = AuthDarkGreen,
                waveColor = HeadGreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .align(Alignment.BottomCenter)
            )

            // ================= FIXED HEADER + SCROLLABLE CONTENT =================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                // Header Gluvi-Edu tetap di atas
                EduScreenTitle()

                Spacer(modifier = Modifier.height(16.dp))

                if (isVideoMode) {
                    // Jangan pakai verticalScroll di sini
                    VideoModeContent(onBackToMenu = onBackToHome)
                } else {
                    // Teks bisa discroll
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        TextModeContent(
                            onWatchVideoClick = { isVideoMode = true }
                        )
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }
    }
}

// ==================== TEXT MODE ====================
@Composable
private fun TextModeContent(onWatchVideoClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Diabetes Melitus",
            color = White,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 20.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Diabetes melitus adalah penyakit kronis akibat gangguan produksi atau kerja insulin, sehingga kadar gula darah meningkat. Jika tidak ditangani, kondisi ini dapat menyebabkan komplikasi serius seperti gangguan jantung dan kerusakan saraf.",
            color = White,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Faktor risiko seperti pola makan tidak sehat, kurang aktivitas fisik, dan faktor genetik berperan besar dalam perkembangan diabetes. Untuk mengetahui lebih lanjut tentang definisi, gejala hingga cara pencegahan dan pengelolaannya, klik Lihat Video Edukasi sekarang dan dapatkan informasi penting untuk menjaga kesehatan anda. Berikut video edukasi mengenai diabetes melitus",
            color = White,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Button(
            onClick = onWatchVideoClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGreen,
                contentColor = White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Lihat Video Edukasi", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun EduScreenTitle() {
    Text(
        text = "Gluvi-Edu",
        color = White,
        fontSize = 40.sp,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    )
}

// ==================== VIDEO MODE ====================
@Composable
private fun VideoModeContent(onBackToMenu: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Video Edukasi",
            color = White,
            fontSize = 26.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    isHorizontalScrollBarEnabled = false
                    overScrollMode = WebView.OVER_SCROLL_NEVER
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true

                    loadUrl("https://www.youtube.com/playlist?list=PLBQtJ5KIa5fEYM06Z6Qm8Bl1ITui7dQRn")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onBackToMenu,
            colors = ButtonDefaults.buttonColors(containerColor = AuthDarkGreen, contentColor = White),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp)
        ) {
            Text("Kembali ke Main Menu", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}

// ==================== WARNA ====================
val EduGreen = Color(0xFF068b6b)
val HeadGreen = Color(0xFF05ab83)
val DarkGreen = Color(0xFF057b61)
