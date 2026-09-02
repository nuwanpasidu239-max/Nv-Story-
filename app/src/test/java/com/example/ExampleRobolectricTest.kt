package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.model.AppCategory
import com.example.data.repository.AppRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var context: Context
    private lateinit var repository: AppRepository
    private lateinit var database: AppDatabase

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext<Application>()
        database = AppDatabase.getInstance(context)
        repository = AppRepository(context, database)
    }

    @Test
    fun `read string from context matches app name`() {
        val appName = context.getString(R.string.app_name)
        assertEquals("AppHub", appName)
    }

    @Test
    fun `repository returns all curated apps`() {
        val apps = repository.getAllApps()
        assertTrue(apps.isNotEmpty())
        val firstApp = apps.first()
        assertNotNull(firstApp.id)
        assertNotNull(firstApp.title)
        assertNotNull(firstApp.apkDownloadUrl)
    }

    @Test
    fun `repository search filters correctly`() {
        val results = repository.searchApps(query = "VLC", category = null, minRating = 0f)
        assertTrue(results.any { it.title.contains("VLC", ignoreCase = true) })
    }

    @Test
    fun `wishlist toggle adds and removes apps`() = runBlocking {
        val testAppId = "vlc_player"
        repository.addToWishlist(testAppId)
        var wishlist = repository.allWishlistAppIds.first()
        assertTrue(wishlist.contains(testAppId))

        repository.removeFromWishlist(testAppId)
        wishlist = repository.allWishlistAppIds.first()
        assertTrue(!wishlist.contains(testAppId))
    }

    @Test
    fun `submit review adds user review`() = runBlocking {
        val testAppId = "vlc_player"
        repository.submitReview(testAppId, "Test User", 5, "Amazing app!")
        val reviews = repository.getReviewsForApp(testAppId).first()
        assertTrue(reviews.any { it.userName == "Test User" && it.comment == "Amazing app!" })
    }
}
