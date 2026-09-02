package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.model.AppCategory
import com.example.data.model.AppItem
import com.example.data.model.RatingBreakdown
import com.example.ui.components.AppGridCard
import com.example.ui.theme.AppHubTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun greeting_screenshot() {
        val sampleApp = AppItem(
            id = "vlc_player",
            packageName = "org.videolan.vlc",
            title = "VLC Media Player",
            developer = "Videolan",
            developerWebsite = "https://www.videolan.org",
            category = AppCategory.ENTERTAINMENT,
            rating = 4.7f,
            reviewsCount = "1.8M",
            downloadsCount = "100M+",
            sizeBytes = 34_500_000L,
            sizeDisplay = "34.5 MB",
            version = "3.5.4",
            summary = "The best open source video and music player.",
            description = "VLC media player is a free and open source cross-platform multimedia player.",
            whatIsNew = "Improved playback performance.",
            releaseDate = "2024-05-10",
            iconUrl = "",
            bannerUrl = "",
            screenshots = emptyList(),
            apkDownloadUrl = "https://get.videolan.org/vlc-android/3.5.4/VLC-Android-3.5.4-arm64-v8a.apk",
            contentRating = "PEGI 3",
            containsAds = false,
            isEditorChoice = true,
            isFeatured = true,
            tags = listOf("Video Players", "Open Source"),
            ratingBreakdown = RatingBreakdown(0.8f, 0.12f, 0.05f, 0.02f, 0.01f)
        )

        composeTestRule.setContent {
            AppHubTheme {
                AppGridCard(
                    app = sampleApp,
                    onAppClick = {}
                )
            }
        }

        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
    }
}
