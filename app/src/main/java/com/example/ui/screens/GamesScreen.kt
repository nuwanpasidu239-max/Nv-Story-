package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.model.AppCategory
import com.example.ui.components.AppGridCard
import com.example.ui.components.AppRowCard
import com.example.ui.components.AppStoreTopSearchBar
import com.example.ui.components.CategoryChipBar
import com.example.ui.components.HeroBannerCarousel
import com.example.ui.viewmodel.AppStoreViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun GamesScreen(
    viewModel: AppStoreViewModel,
    modifier: Modifier = Modifier
) {
    val activeTasks by viewModel.activeTasks.collectAsState()
    val allApps = viewModel.repository.getAllApps()
    val games = allApps.filter { it.category == AppCategory.GAMES }
    val featuredGames = games.filter { it.isFeatured }
    val activeDownloadCount = activeTasks.values.count { it.status.name == "DOWNLOADING" || it.status.name == "QUEUED" }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Top Search Bar
        item {
            AppStoreTopSearchBar(
                activeDownloadCount = activeDownloadCount,
                onSearchClick = { viewModel.navigateTo(ScreenDestination.Search()) },
                onDownloadsClick = { viewModel.navigateTo(ScreenDestination.Downloads) },
                onWishlistClick = { viewModel.navigateTo(ScreenDestination.Wishlist) },
                onSettingsClick = { viewModel.navigateTo(ScreenDestination.Settings) }
            )
        }

        // Hero Games
        item {
            HeroBannerCarousel(
                featuredApps = if (featuredGames.isNotEmpty()) featuredGames else games,
                onAppClick = { app -> viewModel.navigateTo(ScreenDestination.AppDetail(app.id)) },
                onInstallClick = { app -> viewModel.startAppDownload(app) }
            )
        }

        // Section: Action & Racing
        item {
            Spacer(modifier = Modifier.height(12.dp))
            SectionHeader(
                title = "High Octane & Action",
                onSeeAllClick = { viewModel.navigateTo(ScreenDestination.TopCharts) }
            )
        }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(games) { app ->
                    AppGridCard(
                        app = app,
                        onAppClick = { viewModel.navigateTo(ScreenDestination.AppDetail(app.id)) }
                    )
                }
            }
        }

        // Section: Strategy & Tactical RPGs
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = "Strategy & Tactical RPGs",
                onSeeAllClick = { viewModel.navigateTo(ScreenDestination.TopCharts) }
            )
        }

        items(games) { app ->
            val task = activeTasks[app.id]
            val isInstalled = viewModel.isAppInstalled(app.packageName)
            AppRowCard(
                app = app,
                downloadTask = task,
                isInstalled = isInstalled,
                onAppClick = { viewModel.navigateTo(ScreenDestination.AppDetail(app.id)) },
                onInstallClick = {
                    if (task?.status?.name == "COMPLETED") {
                        viewModel.installDownloadedApk(task)
                    } else {
                        viewModel.startAppDownload(app)
                    }
                },
                onOpenClick = { viewModel.openInstalledApp(app.packageName) }
            )
        }
    }
}
