package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppItem
import com.example.data.model.DownloadStatus
import com.example.data.model.ReviewItem
import com.example.ui.components.AppGridCard
import com.example.ui.components.AppIconThumbnail
import com.example.ui.components.RatingBreakdownView
import com.example.ui.components.ScreenshotsGallery
import com.example.ui.theme.PlayGold
import com.example.ui.theme.PlayGreen
import com.example.ui.theme.PlayGreenLight
import com.example.ui.viewmodel.AppStoreViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AppDetailScreen(
    appId: String,
    viewModel: AppStoreViewModel,
    modifier: Modifier = Modifier
) {
    val app = viewModel.repository.getAppById(appId)
    if (app == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("App not found", style = MaterialTheme.typography.titleMedium)
        }
        return
    }

    val activeTasks by viewModel.activeTasks.collectAsState()
    val wishlistAppIds by viewModel.wishlistAppIds.collectAsState()
    val isWishlisted = wishlistAppIds.contains(app.id)
    val task = activeTasks[app.id]
    val isInstalled = viewModel.isAppInstalled(app.packageName)
    val similarApps = viewModel.repository.getSimilarApps(app.id)

    val reviews by viewModel.repository.getReviewsForApp(app.id).collectAsState(initial = emptyList())

    var isDescriptionExpanded by remember { mutableStateOf(false) }
    var showReviewDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleWishlist(app.id) },
                        modifier = Modifier.testTag("detail_wishlist_button")
                    ) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Wishlist",
                            tint = if (isWishlisted) PlayGreen else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = { viewModel.shareApp(app) },
                        modifier = Modifier.testTag("detail_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header: Icon + Title + Developer
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppIconThumbnail(
                        app = app,
                        modifier = Modifier
                            .size(76.dp)
                            .clip(RoundedCornerShape(18.dp))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = app.title,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = app.developer,
                                style = MaterialTheme.typography.bodyMedium,
                                color = PlayGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified",
                                tint = PlayGreen,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = if (app.containsAds) "Contains ads • In-app purchases" else "No ads • Free",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Quick Stats Row: Rating, Reviews, Size, Age Rating, Downloads
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rating
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${app.rating}",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = PlayGold,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Text(
                            text = "${app.reviewsCount} reviews",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Divider(
                        modifier = Modifier
                            .height(24.dp)
                            .width(1.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )

                    // Size
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = app.sizeDisplay,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Download size",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Divider(
                        modifier = Modifier
                            .height(24.dp)
                            .width(1.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )

                    // Content Rating
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.padding(bottom = 2.dp)
                        ) {
                            Text(
                                text = app.contentRating,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                        Text(
                            text = "Rated for 3+",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Divider(
                        modifier = Modifier
                            .height(24.dp)
                            .width(1.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )

                    // Downloads
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = app.downloadsCount,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Downloads",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Primary Action Button: Install / Downloading Progress / Open
            item {
                when {
                    task != null && task.status == DownloadStatus.DOWNLOADING -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Downloading... ${task.progress}% (${String.format("%.1f", task.speedKbps / 1024)} MB/s)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = PlayGreen
                                )

                                Row {
                                    IconButton(
                                        onClick = { viewModel.pauseDownload(app.id) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Pause,
                                            contentDescription = "Pause",
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    IconButton(
                                        onClick = { viewModel.cancelDownload(app.id) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Cancel",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            LinearProgressIndicator(
                                progress = { task.progress / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = PlayGreen,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }

                    task != null && task.status == DownloadStatus.PAUSED -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.resumeDownload(app.id) },
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PlayGreen)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Resume Download")
                            }

                            OutlinedButton(
                                onClick = { viewModel.cancelDownload(app.id) },
                                modifier = Modifier.height(48.dp),
                                shape = RoundedCornerShape(24.dp)
                            ) {
                                Text("Cancel")
                            }
                        }
                    }

                    task != null && task.status == DownloadStatus.COMPLETED -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.installDownloadedApk(task) },
                                modifier = Modifier.weight(1f).height(48.dp).testTag("detail_install_button"),
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PlayGreen)
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Install APK (${app.sizeDisplay})", fontWeight = FontWeight.Bold)
                            }

                            if (isInstalled) {
                                OutlinedButton(
                                    onClick = { viewModel.openInstalledApp(app.packageName) },
                                    modifier = Modifier.height(48.dp),
                                    shape = RoundedCornerShape(24.dp)
                                ) {
                                    Text("Open")
                                }
                            }
                        }
                    }

                    isInstalled -> {
                        Button(
                            onClick = { viewModel.openInstalledApp(app.packageName) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("detail_open_button"),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PlayGreen)
                        ) {
                            Icon(Icons.Default.OpenInNew, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Open App", fontWeight = FontWeight.Bold)
                        }
                    }

                    else -> {
                        Button(
                            onClick = { viewModel.startAppDownload(app) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("detail_download_button"),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PlayGreen)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Install (${app.sizeDisplay})", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Screenshots Section
            item {
                ScreenshotsGallery(screenshots = app.screenshots)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // About this app section
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "About this app",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        IconButton(onClick = { isDescriptionExpanded = !isDescriptionExpanded }) {
                            Icon(
                                imageVector = if (isDescriptionExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Expand"
                            )
                        }
                    }

                    Text(
                        text = app.summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    AnimatedVisibility(visible = isDescriptionExpanded) {
                        Column(modifier = Modifier.padding(top = 8.dp)) {
                            Text(
                                text = app.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // App tags
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                app.tags.forEach { tag ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                    ) {
                                        Text(
                                            text = tag,
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // What's new changelog
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "What's new",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = "Version ${app.version}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = app.whatIsNew,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Data safety badge
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Data Safety",
                            tint = PlayGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Data safety verified",
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = "No data shared with third parties. Verified safe by Play Protect.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Ratings and reviews section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ratings and reviews",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                        fontWeight = FontWeight.Bold
                    )

                    TextButton(onClick = { showReviewDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.RateReview,
                            contentDescription = null,
                            tint = PlayGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Write a review", color = PlayGreen, fontWeight = FontWeight.Bold)
                    }
                }

                RatingBreakdownView(
                    rating = app.rating,
                    reviewsCount = app.reviewsCount,
                    breakdown = app.ratingBreakdown
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            // List of user reviews
            items(reviews) { review ->
                ReviewCardItem(review = review)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Similar Apps Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Similar apps",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(similarApps) { similar ->
                        AppGridCard(
                            app = similar,
                            onAppClick = { viewModel.navigateTo(ScreenDestination.AppDetail(similar.id)) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Interactive Review Dialog
    if (showReviewDialog) {
        WriteReviewDialog(
            appTitle = app.title,
            onDismiss = { showReviewDialog = false },
            onSubmit = { name, rating, comment ->
                viewModel.submitReview(app.id, name, rating, comment)
                showReviewDialog = false
            }
        )
    }
}

@Composable
fun ReviewCardItem(review: ReviewItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (review.isUserSubmitted) PlayGreen else MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = review.userAvatarInitial,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = review.userName,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = review.date,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row {
                    repeat(5) { starIndex ->
                        Icon(
                            imageVector = if (starIndex < review.rating) Icons.Default.Star else Icons.Outlined.StarOutline,
                            contentDescription = null,
                            tint = if (starIndex < review.rating) PlayGold else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun WriteReviewDialog(
    appTitle: String,
    onDismiss: () -> Unit,
    onSubmit: (userName: String, rating: Int, comment: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Rate $appTitle", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Tap stars to rate:", style = MaterialTheme.typography.bodySmall)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(5) { index ->
                        val starNumber = index + 1
                        IconButton(onClick = { rating = starNumber }) {
                            Icon(
                                imageVector = if (starNumber <= rating) Icons.Default.Star else Icons.Outlined.StarOutline,
                                contentDescription = "Star $starNumber",
                                tint = if (starNumber <= rating) PlayGold else MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Your Name (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Describe your experience") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (comment.isNotBlank()) {
                        onSubmit(name.ifBlank { "AppHub Reviewer" }, rating, comment)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PlayGreen),
                enabled = comment.isNotBlank()
            ) {
                Text("Post Review")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
