package com.mrifdnp.gluvia.ui.screen


import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import com.auth0.android.jwt.JWT
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.mrifdnp.gluvia.ui.viewmodel.SignInViewModel
import com.mrifdnp.gluvia.ui.viewmodel.SignUpViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.ExternalAuthConfigDefaults
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.ktor.client.request.request
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.GlobalContext
import java.security.MessageDigest
import java.util.UUID

// Warna yang digunakan (mengambil dari definisi Anda sebelumnya)

val LinkColor = Color(0xFF389F77)
val ButtonColor = Color(0xFFF1F1F1)



enum class AuthScreenState {
    SIGN_UP_OPTIONS,
    SIGN_UP_FORM,
    SIGN_IN_FORM
}

// 🔑 KOMPONEN GOOGLE AUTH EXECUTOR
@Composable
fun GoogleAuthExecutor(
    viewModel: SignInViewModel, // HANYA MENGGUNAKAN SIGN IN VIEW MODEL
    onAuthSuccess: (token: String, nonce: String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Logic untuk Nonce dan Request
    val rawNonce = remember { UUID.randomUUID().toString() }
    val hashedNonce = remember(rawNonce) {
        val digest = MessageDigest.getInstance("SHA-256")
        digest.digest(rawNonce.toByteArray()).fold("") { str, it -> str + "%02x".format(it) }
    }

    val googleIdOption = remember(hashedNonce) {
        GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("931575962864-056lr271sgjfiiadp8m9su27832e3hev.apps.googleusercontent.com")
            .setNonce(hashedNonce)
            .build()
    }

    val request = remember(googleIdOption) {
        GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    // Gunakan LaunchedEffect untuk memicu proses otentikasi
    LaunchedEffect(viewModel) {
        coroutineScope.launch {
            try {
                val credentialsManager = CredentialManager.create(context)
                val result = credentialsManager.getCredential(
                    request = request,
                    context = context,
                )

                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                val googleIdToken = googleIdTokenCredential.idToken

                onAuthSuccess(googleIdToken, rawNonce)
                Toast.makeText(context, "Google Auth Berhasil!", Toast.LENGTH_SHORT).show()

            } catch(e: GetCredentialException) {
                Log.e("GOOGLE_AUTH", "GetCredentialException: ${e.message}")
                Toast.makeText(context, "Gagal otentikasi Google: ${e.message}", Toast.LENGTH_LONG).show()
            } catch(e: GoogleIdTokenParsingException) {
                Log.e("GOOGLE_AUTH", "ParsingException: ${e.message}")
                Toast.makeText(context, "Terjadi kesalahan parsing token.", Toast.LENGTH_SHORT).show()
            } catch(e: Exception) {
                Log.e("GOOGLE_AUTH", "General Exception: ${e.message}")
                Toast.makeText(context, "Terjadi kesalahan umum.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Tampilan Loading
    CircularProgressIndicator(modifier = Modifier.size(32.dp).padding(vertical = 16.dp), color = AuthDarkGreen)
}


@Composable
fun AuthScreen(onNavigateToHome: () -> Unit = {}, ) {
    var currentScreenState by remember { mutableStateOf(AuthScreenState.SIGN_UP_OPTIONS) }

    // State sementara untuk memicu Auth Executor
    var triggerGoogleAuth by remember { mutableStateOf(false) }

    // Ambil ViewModels
    val signUpViewModel: SignUpViewModel = koinViewModel()
    val signInViewModel: SignInViewModel = koinViewModel()

    // Set Navigasi Home pada kedua ViewModels
    LaunchedEffect(Unit) {
        signInViewModel.onNavigateToHome = onNavigateToHome
        signUpViewModel.onNavigateToHome = onNavigateToHome
    }

    // 🔑 Lambda untuk memicu Google Auth, selalu menggunakan SignInViewModel
    val onGoogleAuthClick = {
        triggerGoogleAuth = true
    }


    Scaffold(
        topBar = { GluviaHeader(onMenuClick = {}, showLogo = false) },
        containerColor = White
    ) { paddingValues ->

        val layoutDirection = LocalLayoutDirection.current

        val columnPadding = Modifier.padding(
            start = paddingValues.calculateStartPadding(layoutDirection),
            top = paddingValues.calculateTopPadding(),
            end = paddingValues.calculateEndPadding(layoutDirection),
            bottom = 0.dp
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
                            // 🔑 Tombol Sign Up Google memicu Auth
                            onGoogleSignUpClick = onGoogleAuthClick,
                            onLoginClick = {
                                currentScreenState = AuthScreenState.SIGN_IN_FORM
                            },
                            onRegisterClick = {
                                currentScreenState = AuthScreenState.SIGN_UP_OPTIONS
                            }

                        )
                    }
                    AuthScreenState.SIGN_UP_FORM -> {
                        SignUpFormContent(
                            onBackToOptions = { currentScreenState = AuthScreenState.SIGN_UP_OPTIONS },
                            onNavigateToHome = onNavigateToHome,
                            onNavigateToSignIn = { currentScreenState = AuthScreenState.SIGN_IN_FORM }
                        )
                    }
                    AuthScreenState.SIGN_IN_FORM -> {
                        SignInFormContent(
                            onBackToOptions = { currentScreenState = AuthScreenState.SIGN_UP_OPTIONS },
                            onRegisterClick = { currentScreenState = AuthScreenState.SIGN_UP_OPTIONS },
                            onNavigateToHome = onNavigateToHome,
                            // 🔑 Tombol Login Google memicu Auth

                        )
                    }
                }
            }

            // 🔑 EXECUTION LAYER UNTUK GOOGLE AUTH
            if (triggerGoogleAuth) {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                    GoogleAuthExecutor(
                        viewModel = signInViewModel, // SELALU MENGGUNAKAN SIGN IN VIEW MODEL
                        onAuthSuccess = { token, nonce ->
                            // Panggil signInWithGoogle di SignInViewModel untuk semua kasus Google
                            signInViewModel.signInWithGoogle(token, nonce)
                            triggerGoogleAuth = false
                        }
                    )
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
    onGoogleSignUpClick: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
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

            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 3. Teks Log in
        Text(
            text = buildAnnotatedString {
                append("Sudah punya akun? ")
                withStyle(style = SpanStyle(color = LinkColor, )) {
                    append("Log in")
                }
            },
            modifier = Modifier
                .padding(bottom = 48.dp)
                // 🔑 Panggil handler login di sini
                .clickable { onLoginClick() }
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
// File: com.mrifdnp.gluvia.ui.screen/AuthScreen.kt (lanjutan)
@Composable
fun SignInOptionsContent(
    onEmailSignInClick: () -> Unit,
    onGoogleSignInClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onBackToOptions: () -> Unit // Kembali ke layar Sign Up Options utama
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))


        // 1. Logo
        Image(
            painter = painterResource(id = R.drawable.logo_sma),
            contentDescription = "Logo Sekolah",
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 2. Judul
        Text(
            text = "Login",
            color = AuthDarkGreen,
            fontSize = 32.sp,

            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 3. Teks Sign Up
        Text(
            text = buildAnnotatedString {
                append("Belum punya akun? ")
                withStyle(style = SpanStyle(color = LinkColor, )) {
                    append("Sign Up")
                }
            },
            modifier = Modifier
                .padding(bottom = 48.dp)
                .clickable { onRegisterClick() }
        )


        // Tombol Google
        AuthButton(
            text = "Login with Google",
            icon = R.drawable.ic_google,
            onClick = onGoogleSignInClick,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Garis Pemisah "atau"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(color = Color.Black, thickness = 5.dp, modifier = Modifier.weight(1f))
            Text(" or ", color = Color(0xFF068b6b), modifier = Modifier.padding(horizontal = 8.dp))
            HorizontalDivider(color = Color.Black, thickness = 5.dp, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol Email
        AuthButton(
            text = "Masuk dengan Email",
            isOutline = true,
            onClick = onEmailSignInClick,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInFormContent(onBackToOptions: () -> Unit, onNavigateToHome: () -> Unit, viewModel: SignInViewModel = koinViewModel(),
                      onRegisterClick: () -> Unit
) {
    // 🔑 STATE INTERNAL UNTUK MENGONTROL TAMPILAN
    var viewState by remember { mutableStateOf(SignInViewState.OPTIONS) }

    // Inisialisasi onNavigateToHome dari Composable
    LaunchedEffect(Unit) {
        viewModel.onNavigateToHome = onNavigateToHome
    }
    val context = LocalContext.current
val coroutineScope = rememberCoroutineScope()
    val supabaseClient: SupabaseClient = GlobalContext.get().get()

    // 🔑 Logic Transisi View
    AnimatedContent(
        targetState = viewState,
        label = "SignInFormTransition"
    ) { targetView ->
        when (targetView) {
            SignInViewState.OPTIONS -> {
                SignInOptionsContent(

                    onEmailSignInClick = { viewState = SignInViewState.EMAIL_FORM }, // 🔑 PINDAH KE FORM
                    onGoogleSignInClick = {


                        val credentialsManager = CredentialManager.create(context)

                        val rawNonce = UUID.randomUUID().toString()
                        val bytes = rawNonce.toByteArray()
                        val nd = MessageDigest.getInstance("SHA-256")
                        val digest = nd.digest(bytes)
                        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }
                        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                            .setFilterByAuthorizedAccounts(false)
                            .setServerClientId("931575962864-056lr271sgjfiiadp8m9su27832e3hev.apps.googleusercontent.com")

                            // nonce string to use when generating a Google ID token
                            .setNonce(hashedNonce)
                            .build()
                        val request: GetCredentialRequest = GetCredentialRequest.Builder()
                            .addCredentialOption(googleIdOption)
                            .build()


                        coroutineScope.launch {
                            try {
                                val result = credentialsManager.getCredential(
                                    request = request,
                                    context = context,
                                )
                                val credential = result.credential
                                val googleIdTokenCredential = GoogleIdTokenCredential
                                    .createFrom(credential.data)
                                val googleIdToken = googleIdTokenCredential.idToken
                                val TAG = "GOOGLE_AUTH_INFO" // Gunakan TAG yang lebih spesifik


                                val userId = googleIdTokenCredential.id              // ID unik pengguna Google (sub)
                                val userName = googleIdTokenCredential.displayName    // Nama pengguna
                                val token = JWT(googleIdToken)
                                val userEmail = token.getClaim("email").asString()
                                // -----------------------------
                                // 🔑 LOGGING DETAIL PENGGUNA DI SINI
                                Log.i(TAG, "Login Google Berhasil!")
                                Log.i(TAG, "--- DATA PENGGUNA ---")
                                Log.i(TAG, "User ID (Sub): $userId")
                                Log.i(TAG, "Nama Tampilan: $userName")
                                Log.i(TAG, "Email: $userEmail")
                                Log.i(TAG, "ID Token (JWT): $googleIdToken")

                                Log.i(TAG, googleIdToken)

                                viewModel.signInWithGoogle(googleIdToken, rawNonce)

                                Toast.makeText(context, "You are signed in!", Toast.LENGTH_SHORT)
                                    .show()

                            }catch(e: GetCredentialException){
                             Toast.makeText(context, e.message, Toast.LENGTH_SHORT)
                            }catch(e: GoogleIdTokenParsingException){
                                Toast.makeText(context, e.message, Toast.LENGTH_SHORT)

                            }

                        }
                    },
                    onRegisterClick = onRegisterClick,
                    onBackToOptions = onBackToOptions
                )
            }
            SignInViewState.EMAIL_FORM -> {
                // 🔑 KODE FORM LOGIN YANG SUDAH ADA DITARUH DI BAWAH INI
                SignInEmailForm(
                    viewModel = viewModel,
                    onBackToOptions = { viewState = SignInViewState.OPTIONS } ,// 🔑 Kembali ke Options
                    onRegisterClick = onRegisterClick
                )
            }
        }
    }
}

// 🔑 DEFINISI VIEW STATE BARU
private enum class SignInViewState { OPTIONS, EMAIL_FORM }


// 🔑 KOMPONEN ASLI FORM LOGIN (Dibuat terpisah)
@Composable
fun SignInEmailForm(viewModel: SignInViewModel, onRegisterClick: () -> Unit, onBackToOptions: () -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // 1. Logo
        Image(
            painter = painterResource(id = R.drawable.logo_sma),
            contentDescription = "Logo Sekolah",
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 2. Judul
        Text(
            text = "Login",
            color = AuthDarkGreen,
            fontSize = 32.sp,

            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 3. Teks Sign Up
        Text(
            text = buildAnnotatedString {
                append("Belum punya akun? ")
                withStyle(style = SpanStyle(color = LinkColor,)) {
                    append("Sign Up")
                }
            },
            modifier = Modifier
                .padding(bottom = 48.dp)
                .clickable { onRegisterClick() }
        )

        // --- TOMBOL KEMBALI (ke Opsi Login) ---

        Spacer(modifier = Modifier.height(16.dp))



        OutlinedTextField(
            value = viewModel.emailValue,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                // Warna ketika field dalam keadaan fokus (saat diklik/diketik)
                focusedBorderColor = LightGray,
                focusedLabelColor = AuthDarkGreen, // Warna label saat fokus
                    focusedTextColor = AuthDarkGreen, // Warna teks input saat fokus
                cursorColor = AuthDarkGreen,      // Warna kursor

                // Warna ketika field dalam keadaan tidak fokus
                unfocusedBorderColor = LightGray, // Garis tepi lebih tipis
                unfocusedLabelColor = AuthDarkGreen.copy(alpha = 0.8f)  // Warna label saat tidak fokus

                // Asumsi warna background default adalah Putih/Transparan
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- FIELD: PASSWORD ---
        OutlinedTextField(
            value = viewModel.passwordValue,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            colors = OutlinedTextFieldDefaults.colors(
                // Warna ketika field dalam keadaan fokus (saat diklik/diketik)
                focusedBorderColor = LightGray,
                focusedLabelColor = AuthDarkGreen, // Warna label saat fokus
                focusedTextColor = AuthDarkGreen, // Warna teks input saat fokus
                cursorColor = AuthDarkGreen,      // Warna kursor

                // Warna ketika field dalam keadaan tidak fokus
                unfocusedBorderColor = LightGray, // Garis tepi lebih tipis
                unfocusedLabelColor = AuthDarkGreen.copy(alpha = 0.8f)  // Warna label saat tidak fokus

                // Asumsi warna background default adalah Putih/Transparan
            ),
            visualTransformation = if (viewModel.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (viewModel.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = viewModel::togglePasswordVisibility) {
                    Icon(imageVector  = image, contentDescription = "Toggle password visibility")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        // 🔑 TAMPILKAN ERROR
        if (viewModel.errorMessage != null) {
            Text(
                text = viewModel.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // --- TOMBOL LOGIN ---
        Button(
            onClick = viewModel::onSignInClicked,
            enabled = !viewModel.isLoading && viewModel.isFormValid,
            colors = ButtonDefaults.buttonColors(
                containerColor = AuthDarkGreen, // 🔑 Latar Belakang (Active)
                contentColor = White,           // 🔑 Warna Teks (Active)
                // Opsional: Warna saat tombol non-aktif (disabled)
                disabledContainerColor = AuthDarkGreen.copy(alpha = 0.5f),
                disabledContentColor = White.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Log in",)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpFormContent(onBackToOptions: () -> Unit,                       onNavigateToSignIn: () -> Unit,
                      onNavigateToHome: () -> Unit,       viewModel: SignUpViewModel = koinViewModel()

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
            text = "Sign Up",
            color = AuthDarkGreen,
            fontSize = 28.sp,

            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            textAlign = TextAlign.Center // 🔑 DITAMBAHKAN: Menengahkan teks

        )

        OutlinedTextField(
            // Nilai diambil dari ViewModel
            value = viewModel.usernameValue,
            // Perubahan dikirim ke ViewModel
            onValueChange = viewModel::onUsernameChange,
            label = { Text("Nama Pengguna (Username)") },
            isError = !viewModel.isUsernameValid, // Validasi dari ViewModel
            // ... (KeyboardOptions)
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                // Warna ketika field dalam keadaan fokus (saat diklik/diketik)
                focusedBorderColor = LightGray,
                focusedLabelColor = AuthDarkGreen, // Warna label saat fokus
                focusedTextColor = AuthDarkGreen, // Warna teks input saat fokus
                cursorColor = AuthDarkGreen,      // Warna kursor

                // Warna ketika field dalam keadaan tidak fokus
                unfocusedBorderColor = LightGray, // Garis tepi lebih tipis
                unfocusedLabelColor = AuthDarkGreen.copy(alpha = 0.8f)  // Warna label saat tidak fokus

                // Asumsi warna background default adalah Putih/Transparan
            )
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
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                // Warna ketika field dalam keadaan fokus (saat diklik/diketik)
                focusedBorderColor = LightGray,
                focusedLabelColor = AuthDarkGreen, // Warna label saat fokus
                focusedTextColor = AuthDarkGreen, // Warna teks input saat fokus
                cursorColor = AuthDarkGreen,      // Warna kursor

                // Warna ketika field dalam keadaan tidak fokus
                unfocusedBorderColor = LightGray, // Garis tepi lebih tipis
                unfocusedLabelColor = AuthDarkGreen.copy(alpha = 0.8f)  // Warna label saat tidak fokus

                // Asumsi warna background default adalah Putih/Transparan
            )
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
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                // Warna ketika field dalam keadaan fokus (saat diklik/diketik)
                focusedBorderColor = LightGray,
                focusedLabelColor = AuthDarkGreen, // Warna label saat fokus
                focusedTextColor = AuthDarkGreen, // Warna teks input saat fokus
                cursorColor = AuthDarkGreen,      // Warna kursor

                // Warna ketika field dalam keadaan tidak fokus
                unfocusedBorderColor = LightGray, // Garis tepi lebih tipis
                unfocusedLabelColor = AuthDarkGreen.copy(alpha = 0.8f)  // Warna label saat tidak fokus

                // Asumsi warna background default adalah Putih/Transparan
            )
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
                    modifier = Modifier.clickable { showDatePicker = true },
                    tint = AuthDarkGreen

                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            colors = OutlinedTextFieldDefaults.colors(
                // WARNA KETIKA FOKUS (Teks dan Border)
                focusedBorderColor = LightGray,
                focusedLabelColor = AuthDarkGreen,
                focusedTextColor = AuthDarkGreen,
                cursorColor = AuthDarkGreen,

                // 🔑 PERBAIKAN: WARNA KETIKA TIDAK FOKUS
                unfocusedBorderColor = LightGray,
                unfocusedLabelColor = AuthDarkGreen.copy(alpha = 0.8f),
                unfocusedTextColor = AuthDarkGreen // 🔑 SET TEXT VALUE COLOR
            )
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
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    // WARNA KETIKA FOKUS
                    focusedBorderColor = AuthDarkGreen,
                    focusedLabelColor = AuthDarkGreen,
                    focusedTextColor = AuthDarkGreen,
                    cursorColor = AuthDarkGreen,

                    // 🔑 PERBAIKAN: WARNA KETIKA TIDAK FOKUS
                    unfocusedBorderColor = Color.Gray,
                    unfocusedLabelColor = AuthDarkGreen.copy(alpha = 0.8f),
                    unfocusedTextColor = AuthDarkGreen // 🔑 SET TEXT VALUE COLOR
                )
            )
            ExposedDropdownMenu(
                expanded = isGenderDropdownExpanded,
                onDismissRequest = { isGenderDropdownExpanded = false }
            ) {
                viewModel.genderOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption,color = AuthDarkGreen)  },
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
            colors = ButtonDefaults.buttonColors(
                containerColor = AuthDarkGreen, // 🔑 Latar Belakang (Active)
                contentColor = White,           // 🔑 Warna Teks (Active)
                // Opsional: Warna saat tombol non-aktif (disabled)
                disabledContainerColor = AuthDarkGreen.copy(alpha = 0.5f),
                disabledContentColor = White.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(8.dp), // 🔑 Asumsi shape yang digunakan di SignUp
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Sign up", )
        }
        Spacer(modifier = Modifier.height(32.dp))


        Text(
            text = buildAnnotatedString {
                append("Sudah punya akun? ")
                withStyle(style = SpanStyle(color = LinkColor, )) {
                    append("Log in")
                }
            },
            modifier = Modifier
                .padding(bottom = 32.dp) // 🔑 Jarak sebelum field input
                .clickable { onNavigateToSignIn() } // 🔑 Panggil handler navigasi baru
        )

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
        SignUpFormContent(onBackToOptions = {},   onNavigateToHome = {}, onNavigateToSignIn = {})
    }
}



@Composable
fun AuthFooter(modifier: Modifier = Modifier) {
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier // 🔑 TAMBAHKAN MODIFIER SEBAGAI PARAMETER OPSIONAL

) {
    val backgroundColor = if (isOutline) White else ButtonColor
    val borderColor = if (isOutline) LinkColor else Color.Transparent
    val textColor = if (isOutline) AuthDarkGreen else Black // Menggunakan AuthDarkGreen untuk tombol outline
    val defaultModifier = Modifier
        .fillMaxWidth(0.8f)
        .height(50.dp)
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier.then(defaultModifier) // Menggabungkan modifier input dengan default width

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
            Text(text,  fontSize = 16.sp)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    AuthScreen(onNavigateToHome = {})
}