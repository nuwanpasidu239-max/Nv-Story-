package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppCategory
import com.example.data.model.AppItem
import com.example.ui.components.AppGridCard
import com.example.ui.components.AppRowCard
import com.example.ui.components.AppStoreTopSearchBar
import com.example.ui.components.CategoryChipBar
import com.example.ui.components.HeroBannerCarousel
import com.example.ui.theme.PlayGreen
import com.example.ui.viewmodel.AppStoreViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun HomeScreen(
    viewModel: AppStoreViewModel,
    modifier: Modifier = Modifier
) {
    val activeTasks by viewModel.activeTasks.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val allApps = viewModel.repository.getAllApps()
    val featuredApps = viewModel.repository.getFeaturedApps()
    val editorChoices = viewModel.repository.getEditorChoiceApps()
    val games = allApps.filter { it.category == AppCategory.GAMES }
    val productivityApps = allApps.filter { it.category == AppCategory.PRODUCTIVITY || it.category == AppCategory.TOOLS }

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

        // Category Filter Chips
        item {
            CategoryChipBar(
                categories = listOf(
                    AppCategory.FOR_YOU,
                    AppCategory.TOP_CHARTS,
                    AppCategory.GAMES,
                    AppCategory.PRODUCTIVITY,
                    AppCategory.TOOLS,
                    AppCategory.ENTERTAINMENT,
                    AppCategory.PHOTOGRAPHY
                ),
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    viewModel.selectCategory(category)
                    if (category == AppCategory.TOP_CHARTS) {
                        viewModel.navigateTo(ScreenDestination.TopCharts)
                    } else if (category == AppCategory.GAMES) {
                        viewModel.navigateTo(ScreenDestination.Games)
                    }
                }
            )
        }

        // Hero Banner Carousel
        item {
            Spacer(modifier = Modifier.height(6.dp))
            HeroBannerCarousel(
                featuredApps = featuredApps,
                onAppClick = { app -> viewModel.navigateTo(ScreenDestination.AppDetail(app.id)) },
                onInstallClick = { app -> viewModel.startAppDownload(app) }
            )
        }

        // Section: Recommended for You
        item {
            SectionHeader(
                title = "Recommended for you",
                onSeeAllClick = { viewModel.navigateTo(ScreenDestination.Apps) }
            )
        }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allApps) { app ->
                    AppGridCard(
                        app = app,
                        onAppClick = { viewModel.navigateTo(ScreenDestination.AppDetail(app.id)) }
                    )
                }
            }
        }

        // Section: Trending Apps & Utilities
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = "Essential Tools & Utilities",
                onSeeAllClick = { viewModel.navigateTo(ScreenDestination.Apps) }
            )
        }

        items(productivityApps.take(4)) { app ->
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

        // Section: Top Games
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = "Top Action & Arcade Games",
                onSeeAllClick = { viewModel.navigateTo(ScreenDestination.Games) }
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

        // Section: Editor's Choice
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = "Editor's Choice",
                onSeeAllClick = { viewModel.navigateTo(ScreenDestination.TopCharts) }
            )
        }

        items(editorChoices.take(4)) { app ->
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

@Composable
fun SectionHeader(
    title: String,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        IconButton(onClick = onSeeAllClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "See All",
                tint = PlayGreen
            )
        }
    }
}
