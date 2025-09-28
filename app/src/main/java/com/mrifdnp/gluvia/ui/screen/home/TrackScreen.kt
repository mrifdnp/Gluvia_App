package com.mrifdnp.gluvia.ui.screen.home




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



@Composable
fun TrackScreen(
    onBackToHome: () -> Unit,
    viewModel: CheckViewModel = koinViewModel()

) {
    val monthlyData: List<MonthlyAverage> = viewModel.monthlyChartData

    // 🔑 KONVERSI DATA UNTUK GRAFIK
    val dynamicChartPoints = monthlyData.map {
        // Sumbu Y = averageGlucose, Sumbu X Label = monthLabel
        LineChartData.Point(it.averageGlucose, it.monthLabel)
    }

    // Siapkan LineChartData
    val dynamicLineChartData = LineChartData(
        points = dynamicChartPoints,
        lineDrawer = SolidLineDrawer(color = Color(0xFF6ce5e8), thickness = 4.dp),
    )

    // Label sumbu X HANYA berisi nama-nama bulan yang ada di data
    val dynamicChartLabels = dynamicChartPoints.map { it.label }

    Scaffold(
        topBar = {

            GluviaHeader(onMenuClick = onBackToHome, showTitle = false)
        },
        containerColor = White
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .background(AuthDarkGreen)
            ) {

                TrackHeader()

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(Color(0xff016f55))
                    // ✅ Menggunakan Modifier.background()
                )
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
                            .padding(horizontal = 24.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Grafik Tingkat Kadar Gula Darah berdasarkan Gluvi-Check",
                            color = Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )


                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                            ,
                            contentAlignment = Alignment.Center
                        ) {


                            if (dynamicChartPoints.isNotEmpty()) { // 🔑 SOLUSI: Cek apakah ada data nyata
                                LineChart(
                                    linesChartData = listOf(dynamicLineChartData),
                                    animation = simpleChartAnimation(),
                                    labels = dynamicChartLabels,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                                )
                            } else {
                                // Pesan jika data riwayat benar-benar kosong
                                Text("Belum ada data riwayat glukosa yang tersimpan.", color = Black)
                            }
                        }



                        }
                    }

                TrackFooter(onBackToHome = onBackToHome)}

            WaveShapeBackground( color = AuthDarkGreen,
                waveColor = LinkColor
                )
            }


        }
    }



@Composable
fun TrackHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AuthDarkGreen)
            .padding(horizontal = 24.dp, vertical = 5.dp)
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