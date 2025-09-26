package com.mrifdnp.gluvia.ui.screen.home

import AuthFooter
import LinkColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahmoud.composecharts.linechart.LineChart
import com.mahmoud.composecharts.linechart.LineChartEntity


import com.mrifdnp.gluvia.R // Untuk logo/gambar
import com.mrifdnp.gluvia.ui.screen.Black
import com.mrifdnp.gluvia.ui.screen.GluviaHeader
import com.mrifdnp.gluvia.ui.screen.WaveShape
import kotlin.math.floor
import kotlin.math.min
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData

import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
import com.github.tehras.charts.line.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.SimpleYAxisDrawer
// Pastikan GluviaHeader dan WaveShape/WaveShapeBackground ada di scope ini
// -----------------------------------------------------------------------
import androidx.compose.ui.unit.sp
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.mrifdnp.gluvia.ui.screen.WaveShapeBackground


// Data chart disesuaikan dengan struktur LineChartData.Point
val gluviChartPoints = listOf(
    LineChartData.Point(100f, "Bln 1"),
    LineChartData.Point(120f, "Bln 2"),
    LineChartData.Point(110f, "Bln 3"),
    LineChartData.Point(102f, "Bln 4"),
    LineChartData.Point(110f, "Bln 5")
)

val lineChartData = LineChartData(
    points = gluviChartPoints,
    // ✅ pointDrawer dan lineDrawer DIDEFINISIKAN DI DALAM LineChartData ✅
    lineDrawer = SolidLineDrawer(color = Color(0xFF6ce5e8), thickness = 4.dp),
)

val chartLabels = gluviChartPoints.map { it.label }
@Composable
fun TrackScreen(
    onBackToHome: () -> Unit
) {


    Scaffold(
        topBar = {
            // Menggunakan GluviaHeader, tombol menu berfungsi sebagai tombol kembali
            GluviaHeader(onMenuClick = onBackToHome)
        },
        containerColor = White
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // Area Konten Utama (Header & Grafik)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Mengambil sisa ruang
                    .verticalScroll(rememberScrollState())
                    .background(AuthDarkGreen) // Header awal hijau
            ) {
                // 1. Judul Layar
                TrackHeader()

                // 2. Kontainer Grafik (Wave Shape di bagian atas)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = AuthDarkGreen,
                            shape = WaveShape() // WaveShape di bagian bawah container hijau
                        )
                        .background(White) // Area grafik berwarna Putih
                        .padding(bottom = 20.dp), // Padding agar grafik tidak menempel di wave
                    contentAlignment = Alignment.TopCenter
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Teks Deskripsi Grafik
                        Text(
                            text = "Grafik Tingkat Kadar Gula Darah berdasarkan Gluvi-Check",
                            color = Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Placeholder Area Grafik (Ganti dengan library grafik sebenarnya)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                            ,
                            contentAlignment = Alignment.Center
                        ) {

                            // 🚨 INTEGRASI LINECHART DI SINI 🚨
                            LineChart(
                                // 🚨 KODE PERBAIKAN DI SINI 🚨
                                linesChartData = listOf(lineChartData), // <-- Gunakan data yang sudah berisi drawer

                                // ❌ HAPUS INI: pointDrawer = FilledCircularPointDrawer(...)
                                // ❌ HAPUS INI: lineDrawer = SolidLineDrawer(...)

                                // ... (sisanya tetap sama: animation, xAxisDrawer, yAxisDrawer, labels)
                                animation = simpleChartAnimation(),
                                labels = chartLabels,

                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            )
                        }


                        // Label Sumbu X (Bulan, Tahun)

                            // Label Sumbu Y (Kadar Gula) - Sumbu Y biasanya ditangani oleh LineChart itu sendiri

                        }
                    }
                // Area Footer (Tombol Kembali)
                TrackFooter(onBackToHome = onBackToHome)}

            WaveShapeBackground(color= AuthDarkGreen)
            }


        }
    }


// Data sampel kadar gula darah (Y: Kadar Gula, X: Bulan)

@Composable
fun TrackHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AuthDarkGreen)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // Logo atau Elemen Lainnya
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Gluvi-Track",
            color = White,
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TrackFooter(onBackToHome: () -> Unit) {
    // Area Footer Bawah (Warna hijau gelap)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(LinkColor)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onBackToHome,
            colors = ButtonDefaults.buttonColors(
                containerColor = AuthDarkGreen,
                contentColor = White
            ),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(50.dp)
        ) {
            Text("Kembali ke Main Menu", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrackScreenPreview() {
    TrackScreen(onBackToHome = {})
}