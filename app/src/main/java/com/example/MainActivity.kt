package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AppBottomNavigationBar
import com.example.ui.screens.AppDetailScreen
import com.example.ui.screens.AppsScreen
import com.example.ui.screens.DownloadsScreen
import com.example.ui.screens.GamesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TopChartsScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.AppHubTheme
import com.example.ui.viewmodel.AppStoreViewModel
import com.example.ui.viewmodel.ScreenDestination
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppHubTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val storeViewModel: AppStoreViewModel = viewModel()
                    AppStoreRoot(viewModel = storeViewModel)
                }
            }
        }
    }
}

@Composable
fun AppStoreRoot(
    viewModel: AppStoreViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val activeTasks by viewModel.activeTasks.collectAsState()
    val wishlistAppIds by viewModel.wishlistAppIds.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Collect toast/feedback events
    LaunchedEffect(viewModel) {
        viewModel.toastMessage.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Handle system back button
    BackHandler(enabled = currentScreen !is ScreenDestination.Home) {
        viewModel.navigateBack()
    }

    val showBottomBar = when (currentScreen) {
        is ScreenDestination.Home,
        is ScreenDestination.Games,
        is ScreenDestination.Apps,
        is ScreenDestination.TopCharts,
        is ScreenDestination.Downloads,
        is ScreenDestination.Wishlist -> true
        else -> false
    }

    val activeCount = activeTasks.values.count {
        it.status.name == "DOWNLOADING" || it.status.name == "QUEUED"
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavigationBar(
                    currentDestination = currentScreen,
                    activeDownloadCount = activeCount,
                    wishlistCount = wishlistAppIds.size,
                    onNavigate = { destination ->
                        viewModel.navigateTo(destination)
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else innerPadding.calculateBottomPadding(),
                    top = innerPadding.calculateTopPadding()
                )
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    is ScreenDestination.Home -> HomeScreen(viewModel = viewModel)
                    is ScreenDestination.Games -> GamesScreen(viewModel = viewModel)
                    is ScreenDestination.Apps -> AppsScreen(viewModel = viewModel)
                    is ScreenDestination.TopCharts -> TopChartsScreen(viewModel = viewModel)
                    is ScreenDestination.Downloads -> DownloadsScreen(viewModel = viewModel)
                    is ScreenDestination.Wishlist -> WishlistScreen(viewModel = viewModel)
                    is ScreenDestination.Settings -> SettingsScreen(viewModel = viewModel)
                    is ScreenDestination.Search -> SearchScreen(viewModel = viewModel)
                    is ScreenDestination.AppDetail -> AppDetailScreen(appId = screen.appId, viewModel = viewModel)
                    is ScreenDestination.Sideload -> DownloadsScreen(viewModel = viewModel)
                    is ScreenDestination.CategoryDetail -> HomeScreen(viewModel = viewModel)
                }
            }
        }
    }
}
