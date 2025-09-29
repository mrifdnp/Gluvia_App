package com.mrifdnp.gluvia.ui.screen.menu


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrifdnp.gluvia.ui.screen.GluviaHeader
import com.mrifdnp.gluvia.ui.screen.WaveShapeBackground
import com.mrifdnp.gluvia.ui.screen.home.AuthDarkGreen
import com.mrifdnp.gluvia.ui.screen.home.White
import com.mrifdnp.gluvia.ui.viewmodel.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

val SecondGreen = Color(0xFF068b6b)


@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    Scaffold(
        topBar = {

            GluviaHeader(
                onMenuClick = onBackClick,
                showTitle = true,
                showLogo = false
            )
        },
        containerColor = SecondGreen
    ) { paddingValues ->
        val layoutDirection = LocalLayoutDirection.current

        val columnPadding = Modifier.padding(
            start = paddingValues.calculateStartPadding(layoutDirection),
            top = paddingValues.calculateTopPadding(),
            end = paddingValues.calculateEndPadding(layoutDirection),
            bottom = 0.dp // Tetap set 0.dp agar footer menempel
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(columnPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f) // Ambil sekitar 18% dari tinggi layar
                    // 💡 Gunakan CornerShape besar agar terlihat membulat di bagian bawah

                    .background(color = AuthDarkGreen) // WARNA PUTIH di belakang Avatar
            )
            // 1. WAVE FOOTER (Ditarik ke Bawah, Sebagai Background)
            // Warna dasar: AuthDarkGreen (diisi dari Box)
            WaveShapeBackground(
                color = AuthDarkGreen,
                waveColor = SecondGreen, // Asumsi: Gelombang Putih
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f) // Mengambil 50% bagian bawah layar
                    .align(Alignment.BottomCenter) // Menempel di bawah Box
            )

            // 2. KONTEN UTAMA (Berada di Lapisan Atas, di-scroll)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()), // Scroll di sini
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileContent(viewModel, onEditProfileClick)

                // Spacer untuk memastikan scroll menjangkau seluruh Wave Footer
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun ProfileContent(viewModel: ProfileViewModel, onEditProfileClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp, bottom = 40.dp),
        horizontalAlignment = Alignment.Start
    ) {


        Image(
            painter = painterResource(id = viewModel.profileImageResId),
            contentDescription = "Foto Profil",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(3.dp, White, CircleShape),
            alignment = Alignment.Center
        )

        Spacer(modifier = Modifier.height(24.dp))


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 4.dp)) {

            Text(
                text = viewModel.username,
                color = White,
                fontSize = 28.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
            IconButton(onClick = onEditProfileClick) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit Username",
                    tint = White

                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp) // Ketinggian tipis untuk garis pemisah
                .background(AuthDarkGreen) // Garis pemisah putih tipis
        )


        Text(
            text = "Profile",
            color = White,
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp,top =8.dp)
        )

        // Tanggal Pembuatan
        Text(
            text = "Tanggal Pembuatan: ${viewModel.profileCreationDate}",
            color = White.copy(alpha = 0.8f),
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )


        Button(
            onClick = onEditProfileClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = AuthDarkGreen,
                contentColor = White
            ),
            border = BorderStroke(1.dp, White),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(45.dp)
        ) {
            Text("Edit Profile", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 4. Deskripsi
        Text(
            text = "Deskripsi",
            color = White,
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),

        )


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .border(2.dp, White, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(AuthDarkGreen.copy(alpha = 0.5f))
                .padding(12.dp)
        ) {
            Text(
                text = viewModel.description,
                color = White,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(onBackClick = {}, onEditProfileClick = {})
}