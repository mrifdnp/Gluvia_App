package com.mrifdnp.gluvia.ui.screen

// --- File: HomeScreen.kt ---


import AuthDarkGreen
import AuthFooter
import FeatureCard
import HomeViewModel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.grid.items as GridItems

val AuthDarkGreen = Color(0xFF016d54)

// Asumsi: Warna yang digunakan (AuthDarkGreen, White, Black) sudah didefinisikan

@Composable
fun HomeScreen(
    // Inject ViewModel
    viewModel: HomeViewModel = viewModel(),onLogout: () -> Unit,onFeatureClick: (route: String) -> Unit
) {
    Scaffold(
        topBar = { GluviaHeader(onMenuClick = onLogout) },
        containerColor = White
    ) { paddingValues ->
        val layoutDirection = LocalLayoutDirection.current

        val columnPadding = Modifier.padding(
            start = paddingValues.calculateStartPadding(layoutDirection),
            top = paddingValues.calculateTopPadding(),
            end = paddingValues.calculateEndPadding(layoutDirection),
            bottom = 0.dp // Tetap set 0.dp agar footer menempel
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(columnPadding)
        ) {
            // Konten Utama (Header Sambutan & Kartu)
            Column(modifier = Modifier.weight(1f),) {
                HomeHeader(userName = viewModel.userName)

                // Kartu diatur dalam Grid
                FeatureGrid(
                    featureList = viewModel.featureCards,
                    onCardClick = onFeatureClick // <-- Teruskan callback
                )
            }

            // Footer (Gunakan WaveShape yang sama dari AuthScreen)
            // Di sini kita ubah Modifier.fillMaxHeight(0.3f) ke tinggi yang lebih rendah
            AuthFooter()
        }
    }
}

// ---

@Composable
fun HomeHeader(userName: String) {
    // Area Header Hijau (Menggantikan wave dari AuthScreen)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AuthDarkGreen)
            .padding(horizontal = 32.dp, vertical = 24.dp)
            .wrapContentHeight()
    ) {
        Text(
            text = "Selamat datang di",
            color = White,
            fontSize = 24.sp,
        )
        Text(
            text = "Gluvia",
            color = White,
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            // Anda bisa tambahkan efek stroke/garis tepi jika diinginkan
        )

        // Pesan sambutan
        Text(
            text = "Halo, $userName!",
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

// ---

@Composable
fun FeatureGrid(featureList: List<FeatureCard>, onCardClick: (route: String) -> Unit) { // <-- Ganti nama parameter di sini
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 Kolom per baris
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Gunakan nama parameter baru:
        GridItems(featureList) { card ->
            FeatureCardItem(card = card, onClick = { onCardClick(card.route) })
        }
    }
}

// ---

@Composable
fun FeatureCardItem(card: FeatureCard, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(1f) // Membuat kartu berbentuk persegi
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Gambar Fitur (Menggunakan ImagePlaceholder/ikon yang sesuai)
            Image(
                painter = painterResource(id = card.iconResId),
                contentDescription = card.description,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(80.dp) // Ukuran Ikon/Gambar
                    .padding(bottom = 8.dp)
            )

            // Judul Fitur
            Text(
                text = card.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = AuthDarkGreen,
                // Tambahan: jika ada teks deskripsi, bisa ditambahkan di sini
            )
        }
    }
}

// ---

// Re-use WaveShapeBackground (Ganti nama AuthFooter agar lebih generik)
@Composable
fun WaveShapeBackground(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .background(AuthDarkGreen)
            .background(
                color = Color.White,
                shape = WaveShape() // Gunakan WaveShape yang sudah Anda definisikan
            )

    )
}

// ---

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onLogout = {}, onFeatureClick = {})
}