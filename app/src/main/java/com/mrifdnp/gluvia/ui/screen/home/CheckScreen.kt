package com.mrifdnp.gluvia.ui.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.* import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrifdnp.gluvia.R
import com.mrifdnp.gluvia.ui.screen.GluviaHeader
import com.mrifdnp.gluvia.ui.screen.WaveShapeBackground
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import com.mrifdnp.gluvia.ui.screen.AuthDarkGreen
import com.mrifdnp.gluvia.ui.viewmodel.CheckViewModel
import org.koin.androidx.compose.koinViewModel
// Hapus import yang tidak digunakan (ProfileViewModel)


// Warna yang digunakan (Dipertahankan)
val AuthDarkGreen = Color(0xFF016d54)
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val RedWarning = Color(0xFFFF4444)
val NormalColor = Color(0xFF6ce5e8)
val SecondGreen = Color(0xFF068b6b)

data class Hospital(
    val name: String,
    val phone: String,
    val address: String,
    val imageResId: Int
)

val gianyarHospitals = listOf(
    Hospital("RSU Ganesha", "0361-4710059", "Jl.Raya Celuk Sukawati", R.drawable.check),
    Hospital("RSU Ari Canti", "0361-982223", "Jl.Raya Mas Ubud", R.drawable.check),
    Hospital("RSU Famili Husada", "0361-8493344", "Jl. Astina Timur Samplangan", R.drawable.check)
)

fun getHospitalsForCounty(county: String): List<Hospital> {
    // 🔑 MOCK LOGIC: Hanya mengembalikan data Gianyar untuk semua pilihan di demo ini
    return if (county == "GIANYAR") gianyarHospitals else emptyList()
}

@Composable
fun CheckScreen(
    onBackToHome: () -> Unit,
    onNavigateToCare: () -> Unit,
    viewModel: CheckViewModel = koinViewModel()
) {
    var phInput by remember { mutableStateOf("") }
    // 🔑 Mengambil state hasil dari ViewModel
    val glucoseValue: Float? = viewModel.calculatedGlukosa
    val isCalculated: Boolean = viewModel.isCalculated

    val displayValue = glucoseValue?.let { String.format("%.2f", it) } ?: "N/A"
    val isNormal = glucoseValue != null && glucoseValue >= 70f && glucoseValue <= 126f

    Scaffold(
        topBar = {
            GluviaHeader(onMenuClick = onBackToHome, showTitle = false, backgroundColor = EduGreen)
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
            // Background Layers (unchanged)

            Image(
                painter = painterResource(id = R.drawable.check),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.Center)
                    .alpha(0.5f),
                contentScale = ContentScale.Fit
            )
            WaveShapeBackground(
                color = AuthDarkGreen,
                waveColor = HeadGreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .align(Alignment.BottomCenter)
            )

            // KONTEN UTAMA
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // 1. Judul Layar
                Text(
                    text = "Gluvi-Check",
                    color = White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                )

                // 🔑 TAMPILAN AWAL (Sebelum Dihitung)
                if (!isCalculated) {
                    InitialCheckContent(
                        phInput = phInput,
                        onPhChange = { phInput = it },
                        onCalculate = { viewModel.onCalculateAndSave(phInput) }
                    )
                } else {
                    // 🔑 TAMPILAN HASIL (Setelah Dihitung)
                    ResultCheckContent(
                        glucoseValue = displayValue,
                        isNormal = isNormal,
                        onNavigateToCare = onNavigateToCare,
                        onBackToHome = onBackToHome
                    )
                }

                // Spacer untuk memastikan scroll menjangkau seluruh Wave Footer
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

// --- 🔑 KOMPONEN BARU: TAMPILAN AWAL ---

@Composable
fun InitialCheckContent(
    phInput: String,
    onPhChange: (String) -> Unit,
    onCalculate: () -> Unit
) {
    // Asumsi R.drawable.ic_ph_droplet adalah ikon pH

    Spacer(modifier = Modifier.height(32.dp))

    Image(
        painter = painterResource(id = R.drawable.ph),
        contentDescription = "pH Droplet Icon",
        modifier = Modifier.size(250.dp).padding(bottom = 32.dp),
        contentScale = ContentScale.Fit
    )

    // 1. Input Field (Hanya field, tanpa tombol hitung di dalamnya)
    OutlinedTextField(
        value = phInput,
        onValueChange = onPhChange,
        label = { Text("Input pH Saliva") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        shape = RoundedCornerShape(40.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AuthDarkGreen,
            unfocusedBorderColor = AuthDarkGreen.copy(alpha = 0.5f),
            focusedLabelColor = AuthDarkGreen,
            unfocusedLabelColor = AuthDarkGreen.copy(alpha = 0.8f),
            focusedTextColor = AuthDarkGreen,
            unfocusedTextColor = AuthDarkGreen,
            cursorColor = AuthDarkGreen,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )

    // 2. Reminder Text Merah
    Spacer(modifier = Modifier.height(32.dp))
    Text(
        text = "! Reminder !",
        color = RedWarning,
        fontSize = 18.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    Text(
        text = "Pengukuran pH saliva dilakukan sesaat setelah bangun tidur pada pagi hari tanpa mengkonsumsi makanan, minuman ataupun menggosok gigi.",
        color = RedWarning,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 40.dp)
    )

    // 3. Tombol Hitung
    Button(
        onClick = onCalculate,
        enabled = phInput.toFloatOrNull() != null,
        colors = ButtonDefaults.buttonColors(containerColor = AuthDarkGreen), // Warna Tombol lebih gelap
        shape = RoundedCornerShape(40.dp),
        modifier = Modifier.fillMaxWidth(0.9f).height(50.dp)
    ) {
        Text("Cari Perkiraan Kadar Gula Darah", color = White, fontWeight = FontWeight.SemiBold)
    }
}

// --- 🔑 KOMPONEN BARU: TAMPILAN HASIL (Untuk membersihkan CheckScreen) ---

@Composable
fun ResultCheckContent(
    glucoseValue: String,
    isNormal: Boolean,
    onNavigateToCare: () -> Unit,
    onBackToHome: () -> Unit
) {
    // Menggabungkan semua komponen hasil dari kode lama
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.check),
            contentDescription = "Ilustrasi Pengecekan Gula Darah",
            modifier = Modifier.fillMaxSize(0.8f)
        )
        ResultBox(glucoseValue = glucoseValue, isNormal = isNormal)
    }

    Spacer(modifier = Modifier.height(24.dp))
    NotesSection(isNormal = isNormal)
    Spacer(modifier = Modifier.height(32.dp))

    CheckActionButton(
        text = "Lihat Fasilitas Kesehatan Terdekat",
        onClick = onNavigateToCare,
        isPrimary = false
    )
    CheckActionButton(
        text = "Kembali ke Home",
        onClick = onBackToHome,
        isPrimary = false
    )
    Spacer(modifier = Modifier.height(40.dp))
}


// --- Komponen Pembantu (Dipertahankan) ---

@Composable
fun ResultBox(glucoseValue: String, isNormal: Boolean) {
    val resultColor = if (isNormal) NormalColor else RedWarning
    Card(
        shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = White), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), modifier = Modifier.fillMaxWidth(0.8f).offset(y = 50.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Kadar Gula Darah Anda:", color = AuthDarkGreen, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(text = "$glucoseValue mg/dL", color = resultColor, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp)
        }
    }
}

@Composable
fun NotesSection(isNormal: Boolean) {
    val statusText = if (isNormal) {
        "Nilai Anda dalam batas normal. Pertahankan pola hidup sehat!"
    } else {
        "⚠️ Kadar gula Anda diluar batas normal! Sebaiknya segera merujuk ke fasilitas kesehatan terdekat."
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "STATUS HASIL", color = if (isNormal) NormalColor else RedWarning, fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
        Text(text = "Rentang Normal: 70 – 126 mg/dL", color = White, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
        Card(colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.9f)), shape = RoundedCornerShape(10.dp), modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(text = statusText, color = if (isNormal) AuthDarkGreen else RedWarning, fontSize = 14.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(12.dp))
        }
    }
}

@Composable
fun CheckActionButton(text: String, onClick: () -> Unit, isPrimary: Boolean) {
    val backgroundColor = if (isPrimary) White else AuthDarkGreen
    val textColor = if (isPrimary) AuthDarkGreen else White


    Button(onClick = onClick, colors = ButtonDefaults.buttonColors(containerColor = backgroundColor, contentColor = textColor), shape = RoundedCornerShape(50.dp),  modifier = Modifier.fillMaxWidth(0.9f).height(50.dp).padding(bottom = 12.dp)) {
        Text(text, fontWeight = FontWeight.SemiBold)
    }
}


@Preview(showBackground = true)
@Composable
fun CheckScreenPreview() {
    CheckScreen(onBackToHome = {}, onNavigateToCare = {})
}