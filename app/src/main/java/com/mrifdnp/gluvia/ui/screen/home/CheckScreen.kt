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
import androidx.compose.runtime.* // 🔑 Import penting untuk state management
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrifdnp.gluvia.R // Pastikan R diimpor
import com.mrifdnp.gluvia.ui.screen.GluviaHeader
import com.mrifdnp.gluvia.ui.viewmodel.ProfileViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mrifdnp.gluvia.ui.viewmodel.CheckViewModel


// Warna yang digunakan (AuthDarkGreen, White, Black) sudah didefinisikan
val AuthDarkGreen = Color(0xFF016d54)
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val RedWarning = Color(0xFFFF4444) // Warna merah untuk peringatan
val NormalColor = Color(0xFF6ce5e8) // Warna untuk hasil normal


@Composable
fun CheckScreen(
    onBackToHome: () -> Unit,
    onNavigateToCare: () -> Unit,
    viewModel: CheckViewModel = koinViewModel()
) {
    // 🔑 STATE UNTUK INPUT DAN HASIL
    var phInput by remember { mutableStateOf("") }
    val glucoseValue: Float? = viewModel.calculatedGlukosa
    val isCalculated: Boolean = viewModel.isCalculated


    // Logika Tampilan
    val displayValue = glucoseValue?.let { String.format("%.2f", it) } ?: "N/A"
    // Rentang normal: 70 – 126 mg/dL
    val isNormal = glucoseValue != null && glucoseValue >= 70f && glucoseValue <= 126f

    Scaffold(
        topBar = {
            GluviaHeader(onMenuClick = onBackToHome, showTitle = false)
        },
        containerColor = AuthDarkGreen
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            // 1. Judul Layar
            Text(
                text = "Gluvi-Check",
                color = White,
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )

            // 🔑 2. INPUT FIELD PH SALIVA
            InputPhSection(
                phInput = phInput,
                onPhChange = { phInput = it },
                onCalculate = {
                    viewModel.onCalculateAndSave(phInput)

                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🔑 3. HASIL DAN ILUSTRASI
            if (isCalculated) {
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

                    // Kotak Hasil Perkiraan Kadar Gula Darah
                    ResultBox(glucoseValue = displayValue, isNormal = isNormal)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 4. Catatan/Notes
                NotesSection(isNormal = isNormal)

                Spacer(modifier = Modifier.height(32.dp))

                // 5. Tombol Aksi (Lihat Fasilitas Kesehatan Terdekat)
                CheckActionButton(
                    text = "Lihat Fasilitas Kesehatan Terdekat",
                    onClick = onNavigateToCare,
                    isPrimary = true
                )

                // 6. Tombol Kembali ke Home
                CheckActionButton(
                    text = "Kembali ke Home",
                    onClick = onBackToHome,
                    isPrimary = false
                )

                Spacer(modifier = Modifier.height(40.dp))
            } else {
                // Teks saat belum dihitung
                Box(modifier = Modifier.fillMaxWidth().height(250.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Masukkan nilai pH Saliva Anda untuk melihat hasil perkiraan.",
                        color = White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(32.dp)
                    )
                }
            }
        }
    }
}

// --- Komponen Pembantu Baru ---

@Composable
fun InputPhSection(phInput: String, onPhChange: (String) -> Unit, onCalculate: () -> Unit) {
    OutlinedTextField(
        value = phInput,
        onValueChange = { newValue ->
            // Filter hanya menerima angka, titik, dan koma
            val filteredValue = newValue.filter { it.isDigit() || it == '.' || it == ',' }
            onPhChange(filteredValue.replace(',', '.')) // Standarisasi koma ke titik
        },
        label = { Text("Input Kadar pH Saliva (Contoh: 7.2)") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = White,
            unfocusedBorderColor = White.copy(alpha = 0.5f),
            focusedLabelColor = White,
            unfocusedLabelColor = White.copy(alpha = 0.8f),
            focusedTextColor = White,
            unfocusedTextColor = White,
            cursorColor = White,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        )
    )
    Spacer(Modifier.height(16.dp))
    Button(
        onClick = onCalculate,
        enabled = phInput.toFloatOrNull() != null,
        colors = ButtonDefaults.buttonColors(containerColor = White),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth(0.9f).height(50.dp)
    ) {
        Text("HITUNG HASIL GLUKOSA", color = AuthDarkGreen, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ResultBox(glucoseValue: String, isNormal: Boolean) {
    val resultColor = if (isNormal) NormalColor else RedWarning

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth(0.8f)

            .offset(y = 50.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Kadar Gula Darah Anda:",
                color = AuthDarkGreen,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Text(
                text = "$glucoseValue mg/dL",
                color = resultColor,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp
            )
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
        Text(
            text = "STATUS HASIL",
            color = if (isNormal) NormalColor else RedWarning,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Rentang Normal: 70 – 126 mg/dL",
            color = White,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.9f)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = statusText,
                color = if (isNormal) AuthDarkGreen else RedWarning,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
fun CheckActionButton(
    text: String,
    onClick: () -> Unit,
    isPrimary: Boolean
) {
    val backgroundColor = if (isPrimary) White else AuthDarkGreen
    val textColor = if (isPrimary) AuthDarkGreen else White
    val borderColor = if (isPrimary) Color.Transparent else White

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        shape = RoundedCornerShape(50.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(50.dp)
            .padding(bottom = 12.dp)
    ) {
        Text(text, fontWeight = FontWeight.SemiBold)
    }
}

// --- Preview ---

@Preview(showBackground = true)
@Composable
fun CheckScreenPreview() {
    CheckScreen(
        onBackToHome = {},
        onNavigateToCare = {}
    )
}