package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownloadDone
import androidx.compose.material.icons.filled.InstallMobile
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Sync
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
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.DownloadStatus
import com.example.data.model.DownloadTask
import com.example.ui.theme.PlayGreen
import com.example.ui.viewmodel.AppStoreViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadsScreen(
    viewModel: AppStoreViewModel,
    modifier: Modifier = Modifier
) {
    val activeTasksMap by viewModel.activeTasks.collectAsState()
    val allTasks = activeTasksMap.values.toList()
    val ongoingTasks = allTasks.filter { it.status == DownloadStatus.DOWNLOADING || it.status == DownloadStatus.PAUSED || it.status == DownloadStatus.QUEUED }
    val completedTasks = allTasks.filter { it.status == DownloadStatus.COMPLETED }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Active (${ongoingTasks.size})", "Completed (${completedTasks.size})", "Sideload APK")

    val sideloadUrl by viewModel.sideloadUrlInput.collectAsState()
    val sideloadName by viewModel.sideloadNameInput.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Download Manager",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) PlayGreen else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.testTag("download_tab_$index")
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> {
                    // Active Downloads
                    if (ongoingTasks.isEmpty()) {
                        EmptyDownloadsState(
                            message = "No active downloads",
                            subMessage = "Apps you download from the store will appear here with real-time speed."
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(ongoingTasks) { task ->
                                ActiveDownloadItemCard(
                                    task = task,
                                    onPause = { viewModel.pauseDownload(task.appId) },
                                    onResume = { viewModel.resumeDownload(task.appId) },
                                    onCancel = { viewModel.cancelDownload(task.appId) }
                                )
                            }
                        }
                    }
                }

                1 -> {
                    // Completed Downloads & APK Manager
                    if (completedTasks.isEmpty()) {
                        EmptyDownloadsState(
                            message = "No downloaded APK files",
                            subMessage = "Downloaded apps and installation packages will be saved here."
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(completedTasks) { task ->
                                CompletedDownloadItemCard(
                                    task = task,
                                    onInstall = { viewModel.installDownloadedApk(task) },
                                    onDelete = { viewModel.deleteDownloadedApk(task.appId) },
                                    onOpenDetails = {
                                        viewModel.navigateTo(ScreenDestination.AppDetail(task.appId))
                                    }
                                )
                            }
                        }
                    }
                }

                2 -> {
                    // Sideload APK Direct Downloader
                    SideloadView(
                        url = sideloadUrl,
                        name = sideloadName,
                        onUrlChange = { viewModel.sideloadUrlInput.value = it },
                        onNameChange = { viewModel.sideloadNameInput.value = it },
                        onStartDownload = { viewModel.startSideloadDownload() }
                    )
                }
            }
        }
    }
}

@Composable
fun ActiveDownloadItemCard(
    task: DownloadTask,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Surface(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    color = PlayGreen
                ) {
                    if (task.iconUrl.isNotBlank()) {
                        AsyncImage(model = task.iconUrl, contentDescription = task.appTitle)
                    } else {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = task.appTitle.take(1),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.appTitle,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    val downloadedMb = String.format("%.1f", task.downloadedBytes / (1024f * 1024f))
                    val totalMb = String.format("%.1f", task.totalBytes / (1024f * 1024f))
                    val speedMb = String.format("%.1f", task.speedKbps / 1024f)

                    Text(
                        text = if (task.status == DownloadStatus.PAUSED) "Paused" else "$downloadedMb MB / $totalMb MB • $speedMb MB/s",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (task.status == DownloadStatus.PAUSED) MaterialTheme.colorScheme.error else PlayGreen
                    )
                }

                // Pause / Resume / Cancel Controls
                Row {
                    if (task.status == DownloadStatus.DOWNLOADING) {
                        IconButton(onClick = onPause, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.Pause, contentDescription = "Pause", tint = MaterialTheme.colorScheme.onSurface)
                        }
                    } else if (task.status == DownloadStatus.PAUSED) {
                        IconButton(onClick = onResume, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Resume", tint = PlayGreen)
                        }
                    }

                    IconButton(onClick = onCancel, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { task.progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = PlayGreen,
                trackColor = MaterialTheme.colorScheme.surface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${task.progress}%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Downloading...",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CompletedDownloadItemCard(
    task: DownloadTask,
    onInstall: () -> Unit,
    onDelete: () -> Unit,
    onOpenDetails: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp)),
                color = PlayGreen
            ) {
                if (task.iconUrl.isNotBlank()) {
                    AsyncImage(model = task.iconUrl, contentDescription = task.appTitle)
                } else {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = task.appTitle.take(1),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.appTitle,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                val sizeMb = String.format("%.1f", task.totalBytes / (1024f * 1024f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = PlayGreen,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Downloaded ($sizeMb MB)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Action Buttons
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = onInstall,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PlayGreen),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.height(34.dp).testTag("package_install_button")
                ) {
                    Icon(Icons.Default.InstallMobile, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Install", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete APK",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun SideloadView(
    url: String,
    name: String,
    onUrlChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onStartDownload: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AddLink,
                            contentDescription = null,
                            tint = PlayGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Download & Sideload APK",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 17.sp),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Paste any direct APK download URL to download and install third-party or developer apps directly to your device.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = onNameChange,
                        label = { Text("App Name (e.g. My Custom App)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = url,
                        onValueChange = onUrlChange,
                        label = { Text("Direct APK Download URL") },
                        placeholder = { Text("https://example.com/app.apk") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onStartDownload,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("sideload_start_button"),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PlayGreen),
                        enabled = url.isNotBlank()
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Download & Install APK", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Text(
                text = "Preset Popular Open-Source APKs",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        item {
            PresetSideloadItem(
                title = "VLC Media Player (ARM64)",
                url = "https://get.videolan.org/vlc-android/3.5.4/VLC-Android-3.5.4-arm64-v8a.apk",
                onSelect = {
                    onNameChange("VLC Media Player")
                    onUrlChange("https://get.videolan.org/vlc-android/3.5.4/VLC-Android-3.5.4-arm64-v8a.apk")
                }
            )
        }

        item {
            PresetSideloadItem(
                title = "NewPipe Media Streaming",
                url = "https://github.com/TeamNewPipe/NewPipe/releases/download/v0.27.2/NewPipe_v0.27.2.apk",
                onSelect = {
                    onNameChange("NewPipe")
                    onUrlChange("https://github.com/TeamNewPipe/NewPipe/releases/download/v0.27.2/NewPipe_v0.27.2.apk")
                }
            )
        }

        item {
            PresetSideloadItem(
                title = "AntennaPod Podcast Manager",
                url = "https://github.com/AntennaPod/AntennaPod/releases/download/3.4.1/AntennaPod_3.4.1.apk",
                onSelect = {
                    onNameChange("AntennaPod")
                    onUrlChange("https://github.com/AntennaPod/AntennaPod/releases/download/3.4.1/AntennaPod_3.4.1.apk")
                }
            )
        }
    }
}

@Composable
fun PresetSideloadItem(
    title: String,
    url: String,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = url,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = onSelect,
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Text("Use URL", fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun EmptyDownloadsState(message: String, subMessage: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.FileDownloadDone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
