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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppCategory
import com.example.ui.components.AppRankCard
import com.example.ui.components.AppStoreTopSearchBar
import com.example.ui.components.CategoryChipBar
import com.example.ui.theme.PlayGreen
import com.example.ui.viewmodel.AppStoreViewModel
import com.example.ui.viewmodel.ScreenDestination

enum class ChartFilter(val label: String) {
    TOP_FREE("Top Free"),
    TRENDING("Trending"),
    TOP_GROSSING("Top Grossing")
}

@Composable
fun TopChartsScreen(
    viewModel: AppStoreViewModel,
    modifier: Modifier = Modifier
) {
    val activeTasks by viewModel.activeTasks.collectAsState()
    var selectedChart by remember { mutableStateOf(ChartFilter.TOP_FREE) }
    var selectedCategory by remember { mutableStateOf(AppCategory.FOR_YOU) }

    val rankedApps = viewModel.repository.getTopCharts(selectedCategory)
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

        // Subcategory filters (Top Free, Trending, Top Grossing)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChartFilter.values().forEach { filter ->
                    val isSelected = filter == selectedChart
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedChart = filter },
                        label = {
                            Text(
                                filter.label,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PlayGreen,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Category Filter Chips
        item {
            CategoryChipBar(
                categories = listOf(
                    AppCategory.FOR_YOU,
                    AppCategory.GAMES,
                    AppCategory.PRODUCTIVITY,
                    AppCategory.TOOLS,
                    AppCategory.ENTERTAINMENT,
                    AppCategory.PHOTOGRAPHY
                ),
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Ranked Apps List
        itemsIndexed(rankedApps) { index, app ->
            val task = activeTasks[app.id]
            val isInstalled = viewModel.isAppInstalled(app.packageName)
            AppRankCard(
                rank = index + 1,
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
