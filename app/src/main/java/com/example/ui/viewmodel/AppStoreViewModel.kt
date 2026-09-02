package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.download.AppDownloadManager
import com.example.data.local.AppDatabase
import com.example.data.model.AppCategory
import com.example.data.model.AppItem
import com.example.data.model.DownloadStatus
import com.example.data.model.DownloadTask
import com.example.data.model.ReviewItem
import com.example.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ScreenDestination {
    object Home : ScreenDestination()
    object Games : ScreenDestination()
    object Apps : ScreenDestination()
    object TopCharts : ScreenDestination()
    object Downloads : ScreenDestination()
    object Wishlist : ScreenDestination()
    object Settings : ScreenDestination()
    object Sideload : ScreenDestination()
    data class Search(val initialQuery: String = "") : ScreenDestination()
    data class AppDetail(val appId: String) : ScreenDestination()
    data class CategoryDetail(val category: AppCategory) : ScreenDestination()
}

class AppStoreViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    val repository = AppRepository(application, database)
    val downloadManager = AppDownloadManager(application, database.downloadDao())

    // Navigation State
    private val _currentScreen = MutableStateFlow<ScreenDestination>(ScreenDestination.Home)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    private val screenBackStack = mutableListOf<ScreenDestination>()

    // Selected Category for Explore Tab
    private val _selectedCategory = MutableStateFlow(AppCategory.FOR_YOU)
    val selectedCategory: StateFlow<AppCategory> = _selectedCategory.asStateFlow()

    // Search Query State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchCategory = MutableStateFlow<AppCategory?>(null)
    val searchCategory: StateFlow<AppCategory?> = _searchCategory.asStateFlow()

    private val _searchMinRating = MutableStateFlow(0f)
    val searchMinRating: StateFlow<Float> = _searchMinRating.asStateFlow()

    // UI Feedback events
    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    // Active Downloads
    val activeTasks: StateFlow<Map<String, DownloadTask>> = downloadManager.activeTasks

    // Wishlist
    val wishlistAppIds: StateFlow<Set<String>> = repository.allWishlistAppIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val wishlistApps: StateFlow<List<AppItem>> = repository.getWishlistApps()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Downloaded & Completed APKs list
    val completedDownloads: StateFlow<List<DownloadTask>> = downloadManager.activeTasks
        .combine(database.downloadDao().getAllDownloads()) { activeMap, dbList ->
            activeMap.values.filter { it.status == DownloadStatus.COMPLETED }.sortedByDescending { it.downloadDate }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Direct Sideload / URL input state
    val sideloadUrlInput = MutableStateFlow("")
    val sideloadNameInput = MutableStateFlow("")

    fun navigateTo(destination: ScreenDestination) {
        if (_currentScreen.value != destination) {
            screenBackStack.add(_currentScreen.value)
            _currentScreen.value = destination
        }
    }

    fun navigateBack(): Boolean {
        return if (screenBackStack.isNotEmpty()) {
            _currentScreen.value = screenBackStack.removeAt(screenBackStack.size - 1)
            true
        } else {
            if (_currentScreen.value !is ScreenDestination.Home) {
                _currentScreen.value = ScreenDestination.Home
                true
            } else {
                false
            }
        }
    }

    fun selectCategory(category: AppCategory) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSearchFilters(category: AppCategory?, minRating: Float) {
        _searchCategory.value = category
        _searchMinRating.value = minRating
    }

    fun startAppDownload(app: AppItem) {
        viewModelScope.launch {
            downloadManager.startDownload(app)
            _toastMessage.emit("Downloading ${app.title}...")
        }
    }

    fun pauseDownload(appId: String) {
        downloadManager.pauseDownload(appId)
    }

    fun resumeDownload(appId: String) {
        downloadManager.resumeDownload(appId)
    }

    fun cancelDownload(appId: String) {
        downloadManager.cancelDownload(appId)
    }

    fun deleteDownloadedApk(appId: String) {
        downloadManager.deleteDownloadedFile(appId)
        viewModelScope.launch {
            _toastMessage.emit("APK file deleted")
        }
    }

    fun installDownloadedApk(task: DownloadTask) {
        val success = downloadManager.installApk(task)
        if (!success) {
            viewModelScope.launch {
                _toastMessage.emit("Unable to open APK installer")
            }
        }
    }

    fun startSideloadDownload() {
        val url = sideloadUrlInput.value.trim()
        val name = sideloadNameInput.value.trim().ifEmpty { "Custom App" }
        if (url.isEmpty()) {
            viewModelScope.launch { _toastMessage.emit("Please enter a valid APK download URL") }
            return
        }

        val customId = "custom_${System.currentTimeMillis()}"
        downloadManager.startDownloadDirect(
            appId = customId,
            packageName = "custom.apk.${System.currentTimeMillis()}",
            appTitle = name,
            iconUrl = "",
            iconResId = null,
            apkUrl = url,
            expectedSizeBytes = 25_000_000L
        )

        sideloadUrlInput.value = ""
        sideloadNameInput.value = ""
        navigateTo(ScreenDestination.Downloads)
        viewModelScope.launch {
            _toastMessage.emit("Started downloading $name")
        }
    }

    fun toggleWishlist(appId: String) {
        viewModelScope.launch {
            val isWishlisted = wishlistAppIds.value.contains(appId)
            if (isWishlisted) {
                repository.removeFromWishlist(appId)
                _toastMessage.emit("Removed from Wishlist")
            } else {
                repository.addToWishlist(appId)
                _toastMessage.emit("Added to Wishlist")
            }
        }
    }

    fun submitReview(appId: String, userName: String, rating: Int, comment: String) {
        viewModelScope.launch {
            repository.submitReview(appId, userName, rating, comment)
            _toastMessage.emit("Review submitted! Thank you for rating.")
        }
    }

    fun isAppInstalled(packageName: String): Boolean {
        return try {
            val pm = getApplication<Application>().packageManager
            pm.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        } catch (e: Exception) {
            false
        }
    }

    fun openInstalledApp(packageName: String) {
        val context: Context = getApplication()
        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(launchIntent)
        } else {
            viewModelScope.launch {
                _toastMessage.emit("Cannot launch $packageName directly.")
            }
        }
    }

    fun shareApp(app: AppItem) {
        val context: Context = getApplication()
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Check out ${app.title} on AppHub!")
            putExtra(Intent.EXTRA_TEXT, "Download ${app.title} by ${app.developer} on AppHub: ${app.developerWebsite}")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val chooser = Intent.createChooser(shareIntent, "Share ${app.title}")
        chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(chooser)
    }
}
