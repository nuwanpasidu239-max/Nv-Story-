package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "downloads")
data class DownloadEntity(
    @PrimaryKey
    val appId: String,
    val packageName: String,
    val appTitle: String,
    val iconUrl: String,
    val iconResId: Int?,
    val apkUrl: String,
    val status: String,
    val progress: Int,
    val downloadedBytes: Long,
    val totalBytes: Long,
    val localFilePath: String?,
    val downloadDate: Long = System.currentTimeMillis()
)
