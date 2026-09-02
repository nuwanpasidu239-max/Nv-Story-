package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PlayGreen
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun AppBottomNavigationBar(
    currentDestination: ScreenDestination,
    activeDownloadCount: Int = 0,
    wishlistCount: Int = 0,
    onNavigate: (ScreenDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val navItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    )

    NavigationBar(
        modifier = modifier,
        windowInsets = WindowInsets.navigationBars,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        // Games Tab
        val isGamesSelected = currentDestination is ScreenDestination.Games
        NavigationBarItem(
            selected = isGamesSelected,
            onClick = { onNavigate(ScreenDestination.Games) },
            colors = navItemColors,
            icon = {
                Icon(
                    imageVector = if (isGamesSelected) Icons.Filled.SportsEsports else Icons.Outlined.SportsEsports,
                    contentDescription = "Games"
                )
            },
            label = {
                Text(
                    "Games",
                    fontWeight = if (isGamesSelected) FontWeight.Bold else FontWeight.Normal
                )
            },
            modifier = Modifier.testTag("nav_games_tab")
        )

        // Apps Tab
        val isAppsSelected = currentDestination is ScreenDestination.Apps || currentDestination is ScreenDestination.Home
        NavigationBarItem(
            selected = isAppsSelected,
            onClick = { onNavigate(ScreenDestination.Apps) },
            colors = navItemColors,
            icon = {
                Icon(
                    imageVector = if (isAppsSelected) Icons.Filled.Apps else Icons.Outlined.Apps,
                    contentDescription = "Apps"
                )
            },
            label = {
                Text(
                    "Apps",
                    fontWeight = if (isAppsSelected) FontWeight.Bold else FontWeight.Normal
                )
            },
            modifier = Modifier.testTag("nav_apps_tab")
        )

        // Top Charts Tab
        val isChartsSelected = currentDestination is ScreenDestination.TopCharts
        NavigationBarItem(
            selected = isChartsSelected,
            onClick = { onNavigate(ScreenDestination.TopCharts) },
            colors = navItemColors,
            icon = {
                Icon(
                    imageVector = if (isChartsSelected) Icons.Filled.Leaderboard else Icons.Outlined.Leaderboard,
                    contentDescription = "Top Charts"
                )
            },
            label = {
                Text(
                    "Top Charts",
                    fontWeight = if (isChartsSelected) FontWeight.Bold else FontWeight.Normal
                )
            },
            modifier = Modifier.testTag("nav_charts_tab")
        )

        // Downloads Tab
        val isDownloadsSelected = currentDestination is ScreenDestination.Downloads || currentDestination is ScreenDestination.Sideload
        NavigationBarItem(
            selected = isDownloadsSelected,
            onClick = { onNavigate(ScreenDestination.Downloads) },
            colors = navItemColors,
            icon = {
                BadgedBox(
                    badge = {
                        if (activeDownloadCount > 0) {
                            Badge(containerColor = PlayGreen, contentColor = Color.White) {
                                Text("$activeDownloadCount", fontSize = 10.sp)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isDownloadsSelected) Icons.Filled.Download else Icons.Outlined.Download,
                        contentDescription = "Downloads"
                    )
                }
            },
            label = {
                Text(
                    "Downloads",
                    fontWeight = if (isDownloadsSelected) FontWeight.Bold else FontWeight.Normal
                )
            },
            modifier = Modifier.testTag("nav_downloads_tab")
        )

        // Wishlist Tab
        val isWishlistSelected = currentDestination is ScreenDestination.Wishlist
        NavigationBarItem(
            selected = isWishlistSelected,
            onClick = { onNavigate(ScreenDestination.Wishlist) },
            colors = navItemColors,
            icon = {
                BadgedBox(
                    badge = {
                        if (wishlistCount > 0) {
                            Badge {
                                Text("$wishlistCount", fontSize = 10.sp)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isWishlistSelected) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Wishlist"
                    )
                }
            },
            label = {
                Text(
                    "Wishlist",
                    fontWeight = if (isWishlistSelected) FontWeight.Bold else FontWeight.Normal
                )
            },
            modifier = Modifier.testTag("nav_wishlist_tab")
        )
    }
}
