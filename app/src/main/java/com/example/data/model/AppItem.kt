package com.example.data.model

data class RatingBreakdown(
    val fiveStar: Float = 0.72f,
    val fourStar: Float = 0.18f,
    val threeStar: Float = 0.05f,
    val twoStar: Float = 0.03f,
    val oneStar: Float = 0.02f
)

enum class AppCategory(val displayName: String, val iconName: String) {
    FOR_YOU("For You", "sparkles"),
    TOP_CHARTS("Top Charts", "trending_up"),
    GAMES("Games", "sports_esports"),
    PRODUCTIVITY("Productivity", "task_alt"),
    TOOLS("Tools & Utilities", "build"),
    SOCIAL("Social & Comms", "chat"),
    ENTERTAINMENT("Entertainment", "movie"),
    PHOTOGRAPHY("Photography", "camera_alt"),
    EDUCATION("Education", "school"),
    FINANCE("Finance", "account_balance_wallet")
}

data class AppItem(
    val id: String,
    val packageName: String,
    val title: String,
    val developer: String,
    val developerWebsite: String = "https://github.com",
    val category: AppCategory,
    val rating: Float,
    val reviewsCount: String,
    val downloadsCount: String,
    val sizeDisplay: String,
    val sizeBytes: Long,
    val iconResId: Int? = null,
    val iconUrl: String = "",
    val bannerResId: Int? = null,
    val bannerUrl: String = "",
    val screenshots: List<String> = emptyList(),
    val summary: String,
    val description: String,
    val whatIsNew: String,
    val version: String,
    val releaseDate: String,
    val minAndroidVersion: String = "Android 8.0+",
    val contentRating: String = "Everyone",
    val containsAds: Boolean = false,
    val inAppPurchases: Boolean = false,
    val isFeatured: Boolean = false,
    val isEditorChoice: Boolean = false,
    val rank: Int = 0,
    val apkDownloadUrl: String,
    val tags: List<String> = emptyList(),
    val ratingBreakdown: RatingBreakdown = RatingBreakdown()
)
