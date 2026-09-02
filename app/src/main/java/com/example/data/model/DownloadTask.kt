package com.example.data.model

enum class DownloadStatus {
    IDLE,
    QUEUED,
    DOWNLOADING,
    PAUSED,
    COMPLETED,
    FAILED,
    INSTALLING
}

data class DownloadTask(
    val appId: String,
    val packageName: String,
    val appTitle: String,
    val iconUrl: String = "",
    val iconResId: Int? = null,
    val apkUrl: String,
    val status: DownloadStatus = DownloadStatus.IDLE,
    val progress: Int = 0, // 0 to 100
    val downloadedBytes: Long = 0L,
    val totalBytes: Long = 0L,
    val speedKbps: Float = 0f,
    val localFilePath: String? = null,
    val errorMessage: String? = null,
    val downloadDate: Long = System.currentTimeMillis()
)
