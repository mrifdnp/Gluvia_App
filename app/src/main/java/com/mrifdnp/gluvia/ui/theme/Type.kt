package com.mrifdnp.gluvia.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import com.mrifdnp.gluvia.R // Pastikan R diimpor
// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
val PoppinsFamily = FontFamily(
    // 🔑 Regular (Normal Weight)
    Font(R.font.poppins_regular, FontWeight.Normal),

    // 🔑 Medium Weight
    Font(R.font.poppins_medium, FontWeight.Medium),

    // 🔑 Bold Weight
    Font(R.font.poppins_bold, FontWeight.Bold)
)
val GaretFamily = FontFamily(
    Font(R.font.garet_book, FontWeight.Normal),
    Font(R.font.garet_heavy, FontWeight.Medium),
)
// Definisikan sistem Typography Anda
val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = PoppinsFamily, // 🔑 Menggunakan Poppins Family
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        // ...
    ),
    titleLarge = TextStyle(
        fontFamily = PoppinsFamily, // 🔑 Menggunakan Poppins Family
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        // ...
    ),

    // ... definisikan gaya teks lainnya
)

val GaretTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = GaretFamily, // 🔑 Menggunakan Poppins Family
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        // ...
    ),
    titleLarge = TextStyle(
        fontFamily = GaretFamily, // 🔑 Menggunakan Poppins Family
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        // ...
    ),
    // ... definisikan gaya teks lainnya
)