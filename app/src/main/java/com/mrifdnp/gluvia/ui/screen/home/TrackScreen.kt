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
import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
import com.mrifdnp.gluvia.ui.screen.AuthDarkGreen


// 🔑 DEFINISI WARNA (Dipertahankan)


@Composable
fun TrackScreen(
    onMenuClick: () -> Unit,
onBackToHome: () -> Unit,
    viewModel: CheckViewModel = koinViewModel()
) {
    // Logic Data Historis


    val monthlyPointDrawer = FilledCircularPointDrawer(
        color = Color(0xFF6ce5e8),

    )
    val dailyPointDrawer = FilledCircularPointDrawer(
        color = Color(0xFF6ce5e8),

    )
    val monthlyData = viewModel.monthlyChartData
    val dailyData = viewModel.dailyChartData

    val totalMonths = monthlyData.size
    val totalDataPoints = dailyData.size // Gunakan jumlah data harian sebagai total record
    val monthlyChartPoints = monthlyData.map {
        LineChartData.Point(it.averageGlucose, it.monthLabel)
    }
    val monthlyLineChartData = LineChartData(
        points = monthlyChartPoints,
        lineDrawer = SolidLineDrawer(color = Color(0xFF6ce5e8), thickness = 4.dp),

    )
    val monthlyChartLabels = monthlyChartPoints.map { it.label }

    // 🔑 2. Data Harian (BARU)

    val dailyChartPoints = dailyData.map {
        LineChartData.Point(it.averageGlucose, it.dayLabel)
    }
    val dailyLineChartData = LineChartData(
        points = dailyChartPoints,
        lineDrawer = SolidLineDrawer(color = Color(0xFF6ce5e8), thickness = 4.dp), // Warna berbeda
    )
    val dailyChartLabels = dailyChartPoints.map { it.label }
    // 🔑 LOGIKA KEPUTUSAN YANG DIPERBAIKI
    val chartToShow = when {
        // Kasus 1: Tidak ada data sama sekali
        totalDataPoints == 0 -> "NO_DATA"

        // Kasus 2: Hanya ada 1 data point (1 hari dan 1 record)
        totalDataPoints == 1 -> "SINGLE_POINT"

        // Kasus 3: Data hanya mencakup 1 bulan (meskipun banyak hari) -> Tampilkan Harian
        // Kita menggunakan dailyChartData di sini karena datanya lebih detail
        totalMonths <= 1 -> "DAILY"

        // Kasus 4: Data mencakup lebih dari 1 bulan -> Tampilkan Bulanan
        totalMonths > 1 -> "MONTHLY"

        else -> "NO_DATA" // Fallback
    }



    Scaffold(
        topBar = {
            GluviaHeader(onMenuClick = onMenuClick, showTitle = false, backgroundColor = SecondGreen)
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


                // 🔑 GRAFIK HARIAN (Jika ada lebih dari satu hari data)
                when (chartToShow) {
                    "MONTHLY" -> {
                        // Gunakan data bulanan (monthlyChartPoints)
                        ChartSection(
                            title = "Rata-Rata Glukosa Bulanan",
                            chartPoints = monthlyData.map { LineChartData.Point(it.averageGlucose, it.monthLabel) },
                            lineChartData = monthlyLineChartData, // Pastikan Anda mendefinisikan ini di atas
                            chartLabels = monthlyChartLabels // Pastikan Anda mendefinisikan ini di atas
                            ,            pointDrawer = monthlyPointDrawer

                        )
                    }
                    "DAILY" -> {
                        // Gunakan data harian (dailyChartPoints)
                        ChartSection(
                            title = "Rata-Rata Glukosa Harian (Detail)",
                            chartPoints = dailyData.map { LineChartData.Point(it.averageGlucose, it.dayLabel) },
                            lineChartData = dailyLineChartData, // Pastikan Anda mendefinisikan ini di atas
                            chartLabels = dailyChartLabels // Pastikan Anda mendefinisikan ini di atas
                        ,            pointDrawer = dailyPointDrawer
                        )
                    }
                    "SINGLE_POINT" -> {
                        // Tampilkan poin tunggal dari data harian
                        SingleDataPointView(dailyData.first().let {
                            LineChartData.Point(it.averageGlucose, it.dayLabel)
                        })
                    }
                    "NO_DATA" -> {
                        Text(
                            text = "Belum ada data riwayat glukosa yang tersimpan.",
                            color = White,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                    TrackFooter(onBackToHome = onBackToHome)
                }


            }


        }
    }


// --- Composable Pembantu ---
@Composable
fun ChartSection(
    title: String,
    chartPoints: List<LineChartData.Point>,
    lineChartData: LineChartData,
    chartLabels: List<String>,
    pointDrawer: com.github.tehras.charts.line.renderer.point.PointDrawer
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AuthDarkGreen)
    ) {
        Text(
            text = title,
            color = White,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)
                .background(color = AuthDarkGreen)
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
                    .padding(horizontal = 24.dp, vertical = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LineChart(
                        linesChartData = listOf(lineChartData),
                        animation = simpleChartAnimation(),
                        labels = chartLabels,
                        pointDrawer = pointDrawer,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }
        }
    }
}

// 🔑 KOMPONEN BARU: Menampilkan Poin Tunggal
@Composable
fun SingleDataPointView(point: LineChartData.Point) {
    Card(
        modifier = Modifier.padding(24.dp),
        colors = CardDefaults.cardColors(containerColor = AuthDarkGreen)
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Data Glukosa Terbaru:", color = White, fontSize = 16.sp)
            Text(
                text = "${point.value} mg/dL",
                color = Color(0xFF6ce5e8),
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text("Dicatat pada ${point.label}", color = White.copy(alpha = 0.8f), fontSize = 14.sp)
        }
    }
}

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

