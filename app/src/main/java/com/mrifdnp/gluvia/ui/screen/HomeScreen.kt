package com.mrifdnp.gluvia.ui.screen

// --- File: HomeScreen.kt ---



import FeatureCard
import HomeViewModel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.grid.items as GridItems
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


val AuthDarkGreen = Color(0xFF016d54)

@Composable
fun HomeScreen(
    // Inject ViewModel
    viewModel: HomeViewModel = koinViewModel(),
    onLogout: () -> Unit,
    onFeatureClick: (route: String) -> Unit

) {  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val featureRoutes = viewModel.featureCards.map { Pair(it.title, it.route) }


    ModalNavigationDrawer(
        drawerState = drawerState,
        // Isi Drawer (Menu Samping)
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = AuthDarkGreen,
                content = {

                    HomeDrawerContent(
                        featureRoutes = featureRoutes,
                        onCloseDrawer = { scope.launch { drawerState.close() } },
                        onNavigate = { route ->
                            scope.launch { drawerState.close() }
                            if (route == "logout_route") {
                                viewModel.onLogoutClicked(onLogout) // Panggil VM, teruskan onLogout
                            } else {
                                onFeatureClick(route) // Navigasi fitur normal
                            }
                        }
                    )
                }
            )
        },
        content = {
    Scaffold(
        topBar = {
            GluviaHeader(
                onMenuClick = { scope.launch { drawerState.open() } },

                showTitle = true,
                showLogo = true
            )
        },
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
            // Konten Utama (Header Sambutan & Kartu)
            Column(modifier = Modifier.weight(1f),) {
                HomeHeader(userName = viewModel.userName)

                // Kartu diatur dalam Grid
                FeatureGrid(
                    featureList = viewModel.featureCards,
                    onCardClick = onFeatureClick // <-- Teruskan callback
                )
            }


            AuthFooter()
        }
    }
        }
    )
}
@Composable
fun HomeDrawerContent(
    featureRoutes: List<Pair<String, String>>,
    onCloseDrawer: () -> Unit,
    onNavigate: (route: String) -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(vertical = 32.dp),
    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(

                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notifications",
                tint = White,
                modifier = Modifier.size(24.dp)
            )

            Box {
                Row(
                    modifier = Modifier.clickable { isDropdownExpanded = true }, // Memicu menu
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(

                        imageVector = Icons.Filled.Person,
                        contentDescription = "Profile",
                        tint = White,
                        modifier = Modifier.size(24.dp)
                    )

                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Pilih Opsi",
                        tint = White,
                        modifier = Modifier.size(24.dp)
                    )
                }



                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false },
                    modifier = Modifier.background(White)
                ) {

                    Text(
                        text = "Pilih Opsi",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AuthDarkGreen)
                            .padding(vertical = 8.dp),
                        color = White
                    )

                    // 2. Setting Akun
                    DropdownMenuItem(
                        text = { Text("Setting Akun", color = Black) },
                        onClick = {
                            isDropdownExpanded = false
                            onNavigate("settings_route")
                        }
                    )


                    DropdownMenuItem(
                        text = { Text("Profile", color = Black) },
                        onClick = {
                            isDropdownExpanded = false
                            onNavigate("profile_route")
                        }
                    )


                    DropdownMenuItem(
                        text = { Text("Log Out", color = Black) },
                        onClick = {
                            isDropdownExpanded = false
                            onNavigate("logout_route")
                        }
                    )
                }
            }

            IconButton(onClick = onCloseDrawer) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close Menu",
                    tint = White
                )
            }
        }


        Text(
            text = "Main Menu",
            color = White,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        )


        Divider(color = White, thickness = 2.dp, modifier = Modifier.padding(horizontal = 16.dp))


        featureRoutes.forEach { (title, route) ->
            Column {
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = title,
                            color = White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    selected = false,
                    onClick = { onNavigate(route) },
                    modifier = Modifier.padding(horizontal = 16.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = AuthDarkGreen,
                        selectedContainerColor = AuthDarkGreen.copy(alpha = 0.8f)
                    )
                )

                Divider(color = White, thickness = 2.dp, modifier = Modifier.padding(horizontal = 16.dp))
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

    }
}


@Composable
fun HomeHeader(userName: String) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AuthDarkGreen)
            .padding(horizontal = 32.dp, vertical = 24.dp)
            .wrapContentHeight()
    ) {
        Text(
            text = "Selamat datang di",
            color = White,
            fontSize = 24.sp,
        )
        Text(
            text = "Gluvia",
            color = White,
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,

        )

        Text(
            text = "Halo, $userName!",
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}



@Composable
fun FeatureGrid(featureList: List<FeatureCard>, onCardClick: (route: String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {

        GridItems(featureList) { card ->
            FeatureCardItem(card = card, onClick = { onCardClick(card.route) })
        }
    }
}



@Composable
fun FeatureCardItem(card: FeatureCard, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = card.iconResId),
                contentDescription = card.description,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 8.dp)
            )

            // Judul Fitur
            Text(
                text = card.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = AuthDarkGreen,

            )
        }
    }
}

// ---

// Re-use WaveShapeBackground (Ganti nama AuthFooter agar lebih generik)
@Composable
fun WaveShapeBackground(color: Color,waveColor: Color,
                         modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .background(color)
            .background(
                color = waveColor,
                shape = WaveShape() // Gunakan WaveShape yang sudah Anda definisikan
            )

    )
}

// ---

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onLogout = {}, onFeatureClick = {})
}