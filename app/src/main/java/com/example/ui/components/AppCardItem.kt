package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.AppItem
import com.example.data.model.DownloadStatus
import com.example.data.model.DownloadTask
import com.example.ui.theme.PlayGold
import com.example.ui.theme.PlayGreen

@Composable
fun AppRowCard(
    app: AppItem,
    downloadTask: DownloadTask?,
    isInstalled: Boolean,
    onAppClick: () -> Unit,
    onInstallClick: () -> Unit,
    onOpenClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onAppClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Icon
        AppIconThumbnail(
            app = app,
            modifier = Modifier.size(58.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        // Title and Subtitle Info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = app.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = app.developer,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${app.rating}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = PlayGold,
                        modifier = Modifier.size(12.dp)
                    )
                }

                Text(
                    text = "•",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = app.category.displayName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "•",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = app.sizeDisplay,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Action Button
        AppActionButton(
            downloadTask = downloadTask,
            isInstalled = isInstalled,
            onInstallClick = onInstallClick,
            onOpenClick = onOpenClick
        )
    }
}

@Composable
fun AppRankCard(
    rank: Int,
    app: AppItem,
    downloadTask: DownloadTask?,
    isInstalled: Boolean,
    onAppClick: () -> Unit,
    onInstallClick: () -> Unit,
    onOpenClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onAppClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rank number
        Text(
            text = "$rank",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(26.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.width(8.dp))

        // App Icon
        AppIconThumbnail(
            app = app,
            modifier = Modifier.size(56.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Title and Subtitle Info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = app.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = app.category.displayName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${app.rating}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = PlayGold,
                        modifier = Modifier.size(11.dp)
                    )
                }

                Text(
                    text = "•",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = app.sizeDisplay,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Action Button
        AppActionButton(
            downloadTask = downloadTask,
            isInstalled = isInstalled,
            onInstallClick = onInstallClick,
            onOpenClick = onOpenClick
        )
    }
}

@Composable
fun AppGridCard(
    app: AppItem,
    onAppClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(105.dp)
            .clickable { onAppClick() }
            .padding(4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        AppIconThumbnail(
            app = app,
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(18.dp))
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = app.title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 2.dp)
        ) {
            Text(
                text = "${app.rating}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = PlayGold,
                modifier = Modifier.size(10.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = app.sizeDisplay,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AppIconThumbnail(
    app: AppItem,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp
    ) {
        if (app.iconUrl.isNotBlank()) {
            AsyncImage(
                model = app.iconUrl,
                contentDescription = app.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PlayGreen),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = app.title.take(1),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
        }
    }
}

@Composable
fun AppActionButton(
    downloadTask: DownloadTask?,
    isInstalled: Boolean,
    onInstallClick: () -> Unit,
    onOpenClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        downloadTask != null && downloadTask.status == DownloadStatus.DOWNLOADING -> {
            Box(
                modifier = modifier.size(36.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { downloadTask.progress / 100f },
                    color = PlayGreen,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "${downloadTask.progress}%",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        downloadTask != null && downloadTask.status == DownloadStatus.COMPLETED -> {
            Button(
                onClick = onInstallClick,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PlayGreen),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                modifier = modifier.height(34.dp).testTag("install_apk_button")
            ) {
                Text("Install", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
        isInstalled -> {
            OutlinedButton(
                onClick = onOpenClick,
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                modifier = modifier.height(34.dp).testTag("open_app_button")
            ) {
                Text("Open", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = PlayGreen)
            }
        }
        else -> {
            Button(
                onClick = onInstallClick,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                modifier = modifier.height(34.dp).testTag("download_app_button")
            ) {
                Text("Install", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PlayGreen)
            }
        }
    }
}
