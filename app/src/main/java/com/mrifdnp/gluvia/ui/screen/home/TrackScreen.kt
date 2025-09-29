package com.mrifdnp.gluvia.ui.screen.home

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrifdnp.gluvia.ui.screen.Black
import com.mrifdnp.gluvia.ui.screen.GluviaHeader
import com.mrifdnp.gluvia.ui.screen.WaveShape
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.mrifdnp.gluvia.data.MonthlyAverage
import com.mrifdnp.gluvia.ui.screen.LinkColor
import com.mrifdnp.gluvia.ui.screen.WaveShapeBackground
import com.mrifdnp.gluvia.ui.viewmodel.CheckViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLayoutDirection
import com.mrifdnp.gluvia.ui.screen.AuthDarkGreen


// 🔑 DEFINISI WARNA (Dipertahankan)


@Composable
fun TrackScreen(
    onBackToHome: () -> Unit,
    viewModel: CheckViewModel = koinViewModel()
) {
    // Logic Data Historis
    val monthlyData: List<MonthlyAverage> = viewModel.monthlyChartData
    val dynamicChartPoints = monthlyData.map {
        LineChartData.Point(it.averageGlucose, it.monthLabel)
    }
    val dynamicLineChartData = LineChartData(
        points = dynamicChartPoints,
        lineDrawer = SolidLineDrawer(color = Color(0xFF6ce5e8), thickness = 4.dp),
    )
    val dynamicChartLabels = dynamicChartPoints.map { it.label }


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
                    .background(color = AuthDarkGreen) // Warna hijau gelap di bagian atas

            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(color = SecondGreen ) // Warna hijau gelap di bagian atas

                )

            }
            // 2. WAVE FOOTER
            WaveShapeBackground(
                color = AuthDarkGreen,
                waveColor = HeadGreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .align(Alignment.BottomCenter)
            )

            // 3. KONTEN UTAMA ASLI (Scrollable Content)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally // Tetap Center untuk konten utama
            ) {
                // 🔑 PERBAIKAN: Spacer dikembalikan ke 32.dp (atau nilai yang lebih masuk akal)

                // START KONTEN ASLI ANDA
                TrackHeader()

                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(AuthDarkGreen)
                )

                Text(
                    text = "Grafik Tingkat Kadar Gula Darah berdasarkan Gluvi-Check",
                    color = White,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 30.dp)
                        .background(color = HeadGreen)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AuthDarkGreen)
                ) {


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = AuthDarkGreen,
                                shape = WaveShape()
                            )
                            .background(White)
                            .padding(bottom = 20.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {


                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {



                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                ,
                                contentAlignment = Alignment.Center
                            ) {
                                if (dynamicChartPoints.isNotEmpty()) {
                                    LineChart(
                                        linesChartData = listOf(dynamicLineChartData),
                                        animation = simpleChartAnimation(),
                                        labels = dynamicChartLabels,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 16.dp, vertical = 12.dp)
                                    )
                                } else {
                                    Text("Belum ada data riwayat glukosa yang tersimpan.", color = Black)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp) // Nilai 64dp dipertahankan
                        .background(HeadGreen) // 🔑 WARNA: AuthDarkGreen
                    )


                    TrackFooter(onBackToHome = onBackToHome)
                }


            }


        }
    }
}

// --- Composable Pembantu ---

@Composable
fun TrackHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SecondGreen)
            .padding(horizontal = 24.dp, vertical = 5.dp)
        // 🔑 KOREKSI: Hapus horizontalAlignment = Alignment.CenterHorizontally agar mojok kiri
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Gluvi-Track",
            color = White,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TrackFooter(onBackToHome: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(HeadGreen)
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