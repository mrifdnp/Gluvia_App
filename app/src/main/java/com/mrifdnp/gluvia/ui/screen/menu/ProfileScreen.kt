// File: com.mrifdnp.gluvia.ui.screen.menu.ProfileScreen.kt

package com.mrifdnp.gluvia.ui.screen.menu

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mrifdnp.gluvia.R
import com.mrifdnp.gluvia.ui.screen.GluviaHeader
import com.mrifdnp.gluvia.ui.screen.WaveShapeBackground
import com.mrifdnp.gluvia.ui.screen.home.AuthDarkGreen
import com.mrifdnp.gluvia.ui.screen.home.White
import com.mrifdnp.gluvia.ui.viewmodel.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

val SecondGreen = Color(0xFF068b6b)

@Composable
fun ProfileScreen(

    onMenuClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    Scaffold(
        topBar = {
            GluviaHeader(
                onMenuClick = onMenuClick,
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
            bottom = 0.dp
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(columnPadding)
        ) {
            // Background atas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
                    .background(color = AuthDarkGreen)
            )

            // Wave footer
            WaveShapeBackground(
                color = AuthDarkGreen,
                waveColor = SecondGreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .align(Alignment.BottomCenter)
            )

            // Konten utama
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileContent(viewModel, onEditProfileClick)
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun ProfileContent(
    viewModel: ProfileViewModel,
    onEditProfileClick: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var tempUsername by remember { mutableStateOf(viewModel.username) }
    var tempDescription by remember { mutableStateOf(viewModel.description) }
    var avatarBytes by remember { mutableStateOf<ByteArray?>(null) }

    val context = LocalContext.current

    // Picker untuk pilih gambar dari gallery
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val input = context.contentResolver.openInputStream(it)
            avatarBytes = input?.readBytes()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp, bottom = 40.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Avatar
        Box(contentAlignment = Alignment.BottomEnd) {
            AsyncImage(
                model = if (avatarBytes != null) BitmapFactory.decodeByteArray(
                    avatarBytes, 0, avatarBytes!!.size
                ) else viewModel.profileImageUrl,
                contentDescription = "Foto Profil",
                placeholder = painterResource(id = R.drawable.ic_profile),
                error = painterResource(id = R.drawable.ic_profile),
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(3.dp, White, CircleShape),
                alignment = Alignment.Center
            )

            if (isEditing) {
                IconButton(
                    onClick = { imagePicker.launch("image/*") },
                    modifier = Modifier
                        .size(32.dp)
                        .background(AuthDarkGreen, CircleShape)
                        .align(Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Ganti Avatar",
                        tint = White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Username
        if (isEditing) {
            OutlinedTextField(
                value = tempUsername,
                onValueChange = { tempUsername = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Username") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = White,
                    unfocusedBorderColor = White,
                    focusedLabelColor = White,
                    unfocusedLabelColor = White,
                    cursorColor = White,
                    focusedTextColor = White,
                    unfocusedTextColor = White
                )
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = viewModel.username,
                    color = White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {  tempUsername = viewModel.username
                    tempDescription = viewModel.description
                    isEditing = true
                }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Profil",
                        tint = White
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(AuthDarkGreen)
        )

        Text(
            text = "Deskripsi",
            color = White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
        )

        if (isEditing) {
            OutlinedTextField(
                value = tempDescription,
                onValueChange = { tempDescription = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = { Text("Tulis deskripsi...") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = White,
                    unfocusedBorderColor = White,
                    focusedLabelColor = White,
                    unfocusedLabelColor = White,
                    cursorColor = White,
                    focusedTextColor = White,
                    unfocusedTextColor = White
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = { isEditing = false },
                    modifier = Modifier.weight(1f)
                        .border(1.dp, White, RectangleShape),
                    shape = RectangleShape,
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = AuthDarkGreen,
                        contentColor = White
                    )
                ) {
                    Text("Batal")
                }
                Button(
                    onClick = {
                        viewModel.saveProfile(tempUsername, tempDescription, avatarBytes)
                        isEditing = false
                    },
                    modifier = Modifier.weight(1f)    .border(1.dp, White, RectangleShape),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = White,
                        contentColor = AuthDarkGreen
                    )
                ) {
                    Text("Simpan")
                }
            }

        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
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
}

