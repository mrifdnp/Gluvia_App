import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import com.mrifdnp.gluvia.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.layout.ContentScale
import com.mrifdnp.gluvia.ui.screen.GluviaHeader
import com.mrifdnp.gluvia.ui.screen.WaveShape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.KeyboardCapitalization
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.rememberDatePickerState
import java.time.Instant
import java.time.ZoneId
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrifdnp.gluvia.ui.viewmodel.SignUpViewModel

// Warna yang digunakan (mengambil dari definisi Anda sebelumnya)
val AuthDarkGreen = Color(0xFF016d54)
val LinkColor = Color(0xFF389F77)
val ButtonColor = Color(0xFFF1F1F1)
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)



enum class AuthScreenState {
    SIGN_UP_OPTIONS,
    SIGN_UP_FORM
}

@Composable
fun AuthScreen(onNavigateToHome: () -> Unit = {}, ) {
    var currentScreenState by remember { mutableStateOf(AuthScreenState.SIGN_UP_OPTIONS) }
    Scaffold(
        topBar = { GluviaHeader(onMenuClick = {}) },
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
            // Area Konten Utama
            AnimatedContent(
                targetState = currentScreenState,
                modifier = Modifier.weight(1f),
                transitionSpec = {
                    // Animasi: slide in/out dari kanan ke kiri, dan fade in/out
                    (slideInHorizontally { fullWidth -> fullWidth } + fadeIn())
                        .togetherWith(slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut())
                },
                label = "AuthScreenTransition"
            ) { targetState ->
                when (targetState) {
                    AuthScreenState.SIGN_UP_OPTIONS -> {

                        AuthContent(
                            onEmailSignUpClick = {
                                currentScreenState = AuthScreenState.SIGN_UP_FORM
                            },
                            onGoogleSignUpClick = {
                                // Contoh: langsung ke form atau ke proses Google
                                currentScreenState = AuthScreenState.SIGN_UP_FORM
                            }
                        )
                    }
                    AuthScreenState.SIGN_UP_FORM -> {
                        // ViewModel secara otomatis disediakan oleh viewModel()
                        SignUpFormContent(
                            onBackToOptions = {
                                currentScreenState = AuthScreenState.SIGN_UP_OPTIONS
                            },
                            onNavigateToHome = onNavigateToHome
                        )
                    }
                }
            }

            // Area Footer (Akan muncul tepat di bawah AuthContent)
            AuthFooter()
        }
    }
}
// ---

@Composable
fun AuthContent(
    modifier: Modifier = Modifier,
    onEmailSignUpClick: () -> Unit, // Handler klik Email
    onGoogleSignUpClick: () -> Unit // Handler klik Google
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // 1. Logo (Menggunakan R.drawable.sakuchang)
        Image(
            painter = painterResource(id = R.drawable.logo_sma),
            contentDescription = "Logo Sekolah",
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 2. Judul
        Text(
            text = "Sign up",
            color = AuthDarkGreen,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 3. Teks Log in
        Text(
            text = buildAnnotatedString {
                append("Sudah punya akun? ")
                withStyle(style = SpanStyle(color = LinkColor, fontWeight = FontWeight.SemiBold)) {
                    append("Log in")
                }
            },
            modifier = Modifier
                .padding(bottom = 48.dp)
                .clickable { /* Aksi navigasi Log in */ }
        )


        AuthButton(
            text = "Sign up with Google",
            icon = R.drawable.ic_google,
            onClick = onGoogleSignUpClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 5. Garis Pemisah "or"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            HorizontalDivider(color = Color.Black, thickness = 5.dp, modifier = Modifier.weight(1f))
            Text(" or ", color = Color(0xFF068b6b), modifier = Modifier.padding(horizontal = 8.dp))
            HorizontalDivider(color = Color.Black, thickness = 5.dp, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 6. Tombol Email
        AuthButton(
            text = "Sign up with email",
            isOutline = true,
            onClick = onEmailSignUpClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpFormContent(onBackToOptions: () -> Unit,  onNavigateToHome: () -> Unit,   viewModel: SignUpViewModel = viewModel()
) { LaunchedEffect(Unit) {
    viewModel.onNavigateToHome = onNavigateToHome
}

    val scrollState = rememberScrollState()

    // 1. State untuk Input Field
    var showDatePicker by remember { mutableStateOf(false) }
    var isGenderDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ... (Tombol Kembali & Judul, seperti kode sebelumnya) ...
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            TextButton(
                onClick = onBackToOptions,
                colors = ButtonDefaults.textButtonColors(contentColor = LinkColor),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("← Kembali ke Pilihan")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Daftar Akun Baru",
            color = AuthDarkGreen,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )

        OutlinedTextField(
            // Nilai diambil dari ViewModel
            value = viewModel.usernameValue,
            // Perubahan dikirim ke ViewModel
            onValueChange = viewModel::onUsernameChange,
            label = { Text("Nama Pengguna (Username)") },
            isError = !viewModel.isUsernameValid, // Validasi dari ViewModel
            // ... (KeyboardOptions)
            modifier = Modifier.fillMaxWidth()
        )
        if (!viewModel.isUsernameValid) {
            // ... (Pesan error)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // --- FIELD: EMAIL ---
        OutlinedTextField(
            value = viewModel.emailValue,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            isError = !viewModel.isEmailValid,
            // ... (KeyboardOptions)
            modifier = Modifier.fillMaxWidth()
        )
        // ... (Pesan error Email)
        Spacer(modifier = Modifier.height(16.dp))

        // --- FIELD: PASSWORD ---
        OutlinedTextField(
            value = viewModel.passwordValue,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Password") },
            isError = !viewModel.isPasswordValid,
            // ... (KeyboardOptions)
            visualTransformation = if (viewModel.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (viewModel.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = viewModel::togglePasswordVisibility) { // Panggil handler di VM
                    Icon(imageVector  = image, contentDescription = "Toggle password visibility")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        // ... (Pesan error Password)
        Spacer(modifier = Modifier.height(16.dp))

        // --- FIELD: TANGGAL LAHIR ---
        OutlinedTextField(
            value = viewModel.dateText, // Ambil teks tanggal yang diformat dari VM
            onValueChange = { /* Kosong */ },
            label = { Text("Tanggal Lahir") },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.CalendarToday,
                    contentDescription = "Pilih Tanggal",
                    modifier = Modifier.clickable { showDatePicker = true }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- FIELD: JENIS KELAMIN ---
        ExposedDropdownMenuBox(
            expanded = isGenderDropdownExpanded,
            onExpandedChange = { isGenderDropdownExpanded = !isGenderDropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                readOnly = true,
                value = viewModel.selectedGender, // Ambil nilai dari VM
                onValueChange = { /* Kosong */ },
                label = { Text("Jenis Kelamin") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isGenderDropdownExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = isGenderDropdownExpanded,
                onDismissRequest = { isGenderDropdownExpanded = false }
            ) {
                viewModel.genderOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            viewModel.onGenderSelected(selectionOption) // Panggil handler di VM
                            isGenderDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- TOMBOL DAFTAR AKHIR ---
        Button(
            onClick = viewModel::onSignUpClicked, // Panggil logika bisnis di VM
            enabled = viewModel.isFormComplete, // Enabled state dari VM
            // ... (Colors dan Shape)
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("DAFTAR SEKARANG", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    // --- DATE PICKER DIALOG ---
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = viewModel.selectedDate ?: Instant.now().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onDateSelected(datePickerState.selectedDateMillis) // Kirim data ke VM
                    showDatePicker = false
                }) {
                    Text("PILIH")
                }
            },
            // ... (Dismiss Button)
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
// Tambahkan Preview untuk Form Konten
@Preview(showBackground = true)
@Composable
fun SignUpFormContentPreview() {
    // Asumsi theme standar, White sudah didefinisikan
    Surface(color = White) {
        SignUpFormContent(onBackToOptions = {},   onNavigateToHome = {})
    }
}



@Composable
fun AuthFooter() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .background(AuthDarkGreen)
            .background(
                color = Color.White,
                shape = WaveShape()
            )
            .padding(
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
    )
}

// ---

@Composable
fun AuthButton(
    text: String,
    icon: Int? = null,
    isOutline: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = if (isOutline) White else ButtonColor
    val borderColor = if (isOutline) LinkColor else Color.Transparent
    val textColor = if (isOutline) AuthDarkGreen else Black // Menggunakan AuthDarkGreen untuk tombol outline

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(50.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                // Gambar Icon (sakuchang)
                Icon(
                    imageVector = ImageVector.vectorResource(id = icon), // Panggil vectorResource
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(40.dp).padding(end = 8.dp)
                )
            }
            Text(text, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    AuthScreen(onNavigateToHome = {})
}