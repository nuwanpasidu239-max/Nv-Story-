package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.UserReviewEntity
import com.example.data.local.WishlistEntity
import com.example.data.model.AppCategory
import com.example.data.model.AppItem
import com.example.data.model.RatingBreakdown
import com.example.data.model.ReviewItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class AppRepository(
    private val context: Context,
    private val database: AppDatabase
) {
    private val wishlistDao = database.wishlistDao()
    private val userReviewDao = database.userReviewDao()
    private val downloadDao = database.downloadDao()

    // Curated catalog of high quality apps and games
    private val appsCatalog: List<AppItem> = listOf(
        AppItem(
            id = "vlc_player",
            packageName = "org.videolan.vlc",
            title = "VLC Media Player",
            developer = "Videolan",
            developerWebsite = "https://www.videolan.org",
            category = AppCategory.ENTERTAINMENT,
            rating = 4.8f,
            reviewsCount = "1.8M",
            downloadsCount = "500M+",
            sizeDisplay = "34 MB",
            sizeBytes = 35_651_584L,
            iconUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=200&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=800&auto=format&fit=crop&q=80",
            screenshots = listOf(
                "https://images.unsplash.com/photo-1518791841217-8f162f1e1131?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=600&auto=format&fit=crop&q=80"
            ),
            summary = "The ultimate open source multimedia player for audio and video on Android.",
            description = "VLC for Android is the best open source video and music player, fast and easy! VLC plays most local video and audio files, as well as network streams (including adaptive streaming) and DVD ISOs. All formats are supported, including MKV, MP4, AVI, MOV, Ogg, FLAC, TS, M2TS, Wv and AAC.",
            whatIsNew = "• Added equalizer presets custom save\n• Improved hardware acceleration for 4K 60fps\n• Fixed subtitle sync on bluetooth headsets\n• Background audio playback optimizations",
            version = "3.5.4",
            releaseDate = "Sep 2025",
            isFeatured = true,
            isEditorChoice = true,
            rank = 1,
            apkDownloadUrl = "https://get.videolan.org/vlc-android/3.5.4/VLC-Android-3.5.4-arm64-v8a.apk",
            tags = listOf("Video Player", "Music Player", "Open Source", "No Ads", "High Definition"),
            ratingBreakdown = RatingBreakdown(0.82f, 0.12f, 0.03f, 0.02f, 0.01f)
        ),
        AppItem(
            id = "cyber_sprint_3d",
            packageName = "com.aistudio.games.cybersprint",
            title = "Cyber Sprint 3D: Neon Runner",
            developer = "NeonPulse Interactive",
            developerWebsite = "https://games.neonpulse.dev",
            category = AppCategory.GAMES,
            rating = 4.9f,
            reviewsCount = "450K",
            downloadsCount = "50M+",
            sizeDisplay = "128 MB",
            sizeBytes = 134_217_728L,
            iconUrl = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=200&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800&auto=format&fit=crop&q=80",
            screenshots = listOf(
                "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1511512578047-dfb367046420?w=600&auto=format&fit=crop&q=80"
            ),
            summary = "High-octane cyberpunk parkour racing through futuristic neon skylines.",
            description = "Sprint, wall-run, and grapple across towering holographic skyscrapers in the year 2099. Collect hyper-cores, upgrade cybernetic implants, and compete in multiplayer leaderboards. Featuring dynamic synthwave soundtracks and ultra-smooth 120 FPS graphics.",
            whatIsNew = "• Season 4: Neo Tokyo Megacity track pack\n• New character skin: Ghost Runner Cipher\n• Added Ray-Tracing lighting effects option\n• Multiplayer tournament weekly mode",
            version = "2.4.1",
            releaseDate = "Aug 2025",
            containsAds = true,
            inAppPurchases = true,
            isFeatured = true,
            isEditorChoice = true,
            rank = 2,
            apkDownloadUrl = "https://raw.githubusercontent.com/apphub-demo/assets/main/cybersprint.apk",
            tags = listOf("Action", "Endless Runner", "Cyberpunk", "3D Graphics", "Multiplayer"),
            ratingBreakdown = RatingBreakdown(0.85f, 0.10f, 0.03f, 0.01f, 0.01f)
        ),
        AppItem(
            id = "newpipe_player",
            packageName = "org.schabi.newpipe",
            title = "NewPipe Streaming",
            developer = "Team NewPipe",
            developerWebsite = "https://newpipe.net",
            category = AppCategory.ENTERTAINMENT,
            rating = 4.7f,
            reviewsCount = "890K",
            downloadsCount = "100M+",
            sizeDisplay = "19 MB",
            sizeBytes = 19_922_944L,
            iconUrl = "https://images.unsplash.com/photo-1611162617474-5b21e879e113?w=200&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=800&auto=format&fit=crop&q=80",
            screenshots = listOf(
                "https://images.unsplash.com/photo-1611162617474-5b21e879e113?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=600&auto=format&fit=crop&q=80"
            ),
            summary = "Lightweight streaming frontend with picture-in-picture, background audio, and offline downloads.",
            description = "NewPipe is a lightweight YouTube frontend for Android that does not use any Google Play Services libraries. Enjoy background playback, popup player (Picture-in-Picture), local playlists, subscriptions without an account, and clean ad-free streaming.",
            whatIsNew = "• Faster stream extraction algorithm\n• Enhanced 4K HDR playback stability\n• Fixed subtitle rendering glitch\n• Added support for chapters preview",
            version = "0.27.2",
            releaseDate = "Oct 2025",
            isFeatured = true,
            isEditorChoice = true,
            rank = 3,
            apkDownloadUrl = "https://github.com/TeamNewPipe/NewPipe/releases/download/v0.27.2/NewPipe_v0.27.2.apk",
            tags = listOf("Streaming", "Video", "Picture-in-Picture", "Privacy", "Lightweight"),
            ratingBreakdown = RatingBreakdown(0.79f, 0.14f, 0.04f, 0.02f, 0.01f)
        ),
        AppItem(
            id = "taskflow_pro",
            packageName = "com.productivity.taskflow",
            title = "TaskFlow Pro: Focus & AI Planner",
            developer = "FlowState Labs",
            developerWebsite = "https://flowstate.io",
            category = AppCategory.PRODUCTIVITY,
            rating = 4.9f,
            reviewsCount = "310K",
            downloadsCount = "25M+",
            sizeDisplay = "24 MB",
            sizeBytes = 25_165_824L,
            iconUrl = "https://images.unsplash.com/photo-1484480974693-6ca0a78fb36b?w=200&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1507925921958-8a62f3d1a50d?w=800&auto=format&fit=crop&q=80",
            screenshots = listOf(
                "https://images.unsplash.com/photo-1484480974693-6ca0a78fb36b?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1507925921958-8a62f3d1a50d?w=600&auto=format&fit=crop&q=80"
            ),
            summary = "Smart task manager with Pomodoro focus timer, Kanban boards, and smart scheduling.",
            description = "Master your day with TaskFlow Pro. Organize projects with intuitive Kanban boards, track habits with calendar heatmaps, and boost deep focus with built-in ambient white noise and Pomodoro intervals.",
            whatIsNew = "• Interactive Home Screen Widgets\n• Markdown support in task descriptions\n• Quick capture notification tile\n• Cloud backup and offline-first sync",
            version = "4.1.0",
            releaseDate = "Sep 2025",
            inAppPurchases = true,
            isFeatured = true,
            isEditorChoice = true,
            rank = 4,
            apkDownloadUrl = "https://raw.githubusercontent.com/apphub-demo/assets/main/taskflow.apk",
            tags = listOf("Productivity", "To-Do List", "Pomodoro", "Habit Tracker", "Kanban"),
            ratingBreakdown = RatingBreakdown(0.88f, 0.08f, 0.02f, 0.01f, 0.01f)
        ),
        AppItem(
            id = "retro_music",
            packageName = "code.name.monkey.retromusic",
            title = "Retro Music Player",
            developer = "Hemanth S",
            developerWebsite = "https://retromusic.app",
            category = AppCategory.ENTERTAINMENT,
            rating = 4.8f,
            reviewsCount = "520K",
            downloadsCount = "15M+",
            sizeDisplay = "16 MB",
            sizeBytes = 16_777_216L,
            iconUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=200&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=800&auto=format&fit=crop&q=80",
            screenshots = listOf(
                "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=600&auto=format&fit=crop&q=80"
            ),
            summary = "Gorgeous Material You design offline audio player with synchronized lyrics and gapless playback.",
            description = "Retro Music Player combines the best of Material Design 3 guidelines with smooth animations, custom themes, floating lyrics, tag editor, and audio sleep timer.",
            whatIsNew = "• Dynamic lyrics syncer\n• New vinyl turntable animation\n• Material 3 expressive color schemes\n• Android Auto car integration",
            version = "6.2.0",
            releaseDate = "Jul 2025",
            isFeatured = false,
            isEditorChoice = true,
            rank = 5,
            apkDownloadUrl = "https://github.com/RetroMusicPlayer/RetroMusicPlayer/releases/download/v6.2.0/RetroMusic_v6.2.0.apk",
            tags = listOf("Audio", "Offline Music", "Hi-Res Audio", "Equalizer", "Material You"),
            ratingBreakdown = RatingBreakdown(0.80f, 0.13f, 0.04f, 0.02f, 0.01f)
        ),
        AppItem(
            id = "osmand_maps",
            packageName = "net.osmand.plus",
            title = "OsmAnd: Offline Maps & GPS",
            developer = "OsmAnd B.V.",
            developerWebsite = "https://osmand.net",
            category = AppCategory.TOOLS,
            rating = 4.6f,
            reviewsCount = "390K",
            downloadsCount = "30M+",
            sizeDisplay = "85 MB",
            sizeBytes = 89_128_960L,
            iconUrl = "https://images.unsplash.com/photo-1524661135-423995f22d0b?w=200&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800&auto=format&fit=crop&q=80",
            screenshots = listOf(
                "https://images.unsplash.com/photo-1524661135-423995f22d0b?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=600&auto=format&fit=crop&q=80"
            ),
            summary = "Global offline GPS navigation with OpenStreetMap worldwide hiking & driving trails.",
            description = "OsmAnd is an offline world map application based on OpenStreetMap (OSM), which allows you to navigate taking into account the preferred roads and vehicle dimensions. Plan routes based on tracks and record GPX tracks without an internet connection.",
            whatIsNew = "• 3D terrain elevation contour lines\n• Turn-by-turn voice navigation in 40+ languages\n• Speed camera alerts update\n• Battery saving routing mode",
            version = "4.8.5",
            releaseDate = "Aug 2025",
            isFeatured = false,
            isEditorChoice = true,
            rank = 6,
            apkDownloadUrl = "https://github.com/osmandapp/OsmAnd/releases/download/v4.8.5/OsmAnd-default-v4.8.5.apk",
            tags = listOf("Navigation", "Maps", "GPS", "Offline", "Hiking"),
            ratingBreakdown = RatingBreakdown(0.74f, 0.17f, 0.05f, 0.02f, 0.02f)
        ),
        AppItem(
            id = "canvas_craft_pro",
            packageName = "com.studio.canvascraft",
            title = "CanvasCraft: Pixel & Vector Art",
            developer = "PixelForge Studios",
            developerWebsite = "https://pixelforge.art",
            category = AppCategory.PHOTOGRAPHY,
            rating = 4.8f,
            reviewsCount = "210K",
            downloadsCount = "10M+",
            sizeDisplay = "42 MB",
            sizeBytes = 44_040_192L,
            iconUrl = "https://images.unsplash.com/photo-1513364776144-60967b0f800f?w=200&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=800&auto=format&fit=crop&q=80",
            screenshots = listOf(
                "https://images.unsplash.com/photo-1513364776144-60967b0f800f?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=600&auto=format&fit=crop&q=80"
            ),
            summary = "Professional digital illustration, sprite animator, and vector drawing canvas.",
            description = "Create stunning digital paintings, sprite animations, comics, and vector logos with pressure sensitivity, infinite layers, blend modes, and over 100 customizable brushes.",
            whatIsNew = "• Added timelapse video export at 4K\n• PSD format import/export\n• Palm rejection sensitivity tuning\n• New watercolor blend engine",
            version = "3.2.1",
            releaseDate = "Sep 2025",
            inAppPurchases = true,
            isFeatured = true,
            isEditorChoice = true,
            rank = 7,
            apkDownloadUrl = "https://raw.githubusercontent.com/apphub-demo/assets/main/canvascraft.apk",
            tags = listOf("Art", "Drawing", "Animation", "Vector", "Design"),
            ratingBreakdown = RatingBreakdown(0.81f, 0.12f, 0.04f, 0.02f, 0.01f)
        ),
        AppItem(
            id = "open_camera",
            packageName = "net.sourceforge.opencamera",
            title = "Open Camera: Pro RAW HDR",
            developer = "Mark Harman",
            developerWebsite = "https://opencamera.org.uk",
            category = AppCategory.PHOTOGRAPHY,
            rating = 4.7f,
            reviewsCount = "670K",
            downloadsCount = "100M+",
            sizeDisplay = "5 MB",
            sizeBytes = 5_242_880L,
            iconUrl = "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=200&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1500485035595-cbe6f645feb1?w=800&auto=format&fit=crop&q=80",
            screenshots = listOf(
                "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1500485035595-cbe6f645feb1?w=600&auto=format&fit=crop&q=80"
            ),
            summary = "Fully featured, completely open source manual camera app for photos and 4K video.",
            description = "Open Camera is a full featured and completely free Camera app. Auto-stabilize, manual ISO, exposure lock, manual focus distance, Camera2 API support, RAW (DNG) capture, and slow-motion video.",
            whatIsNew = "• Support for 10-bit HDR video recording\n• Histogram overlay toggle in viewfinder\n• Audio level meter for external microphones\n• Speed improvements for burst capture",
            version = "1.53.2",
            releaseDate = "Jul 2025",
            isFeatured = false,
            isEditorChoice = false,
            rank = 8,
            apkDownloadUrl = "https://sourceforge.net/projects/opencamera/files/opencamera_v1.53.2.apk/download",
            tags = listOf("Camera", "RAW Photography", "Manual Focus", "HDR Video", "Open Source"),
            ratingBreakdown = RatingBreakdown(0.76f, 0.16f, 0.05f, 0.02f, 0.01f)
        ),
        AppItem(
            id = "starfall_chronicles",
            packageName = "com.rpg.starfallchronicles",
            title = "Starfall Chronicles: Galaxy RPG",
            developer = "Aetheria Interactive",
            developerWebsite = "https://aetheria.games",
            category = AppCategory.GAMES,
            rating = 4.9f,
            reviewsCount = "380K",
            downloadsCount = "10M+",
            sizeDisplay = "310 MB",
            sizeBytes = 325_058_560L,
            iconUrl = "https://images.unsplash.com/photo-1538481199705-c710c4e965fc?w=200&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=800&auto=format&fit=crop&q=80",
            screenshots = listOf(
                "https://images.unsplash.com/photo-1538481199705-c710c4e965fc?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=600&auto=format&fit=crop&q=80"
            ),
            summary = "Epic open-world space tactical RPG with starship customization and deep storylines.",
            description = "Explore uncharted star systems, recruit a crew of alien specialists, trade resources across warp gates, and engage in tactical fleet battles in this award-winning space roleplaying adventure.",
            whatIsNew = "• Expansion Pack: The Void Nebula\n• Added 12 new starship classes\n• Guild co-op raid boss battles\n• Enhanced spatial audio soundtrack",
            version = "1.8.0",
            releaseDate = "Aug 2025",
            inAppPurchases = true,
            isFeatured = true,
            isEditorChoice = true,
            rank = 9,
            apkDownloadUrl = "https://raw.githubusercontent.com/apphub-demo/assets/main/starfall.apk",
            tags = listOf("RPG", "Sci-Fi", "Space", "Tactics", "Story Rich"),
            ratingBreakdown = RatingBreakdown(0.89f, 0.08f, 0.02f, 0.01f, 0.00f)
        ),
        AppItem(
            id = "antennapod",
            packageName = "de.danoeh.antennapod",
            title = "AntennaPod: Podcast Player",
            developer = "AntennaPod Open Source",
            developerWebsite = "https://antennapod.org",
            category = AppCategory.ENTERTAINMENT,
            rating = 4.8f,
            reviewsCount = "190K",
            downloadsCount = "5M+",
            sizeDisplay = "14 MB",
            sizeBytes = 14_680_064L,
            iconUrl = "https://images.unsplash.com/photo-1590602847861-f357a9332bbc?w=200&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1478737270239-2f02b77fc618?w=800&auto=format&fit=crop&q=80",
            screenshots = listOf(
                "https://images.unsplash.com/photo-1590602847861-f357a9332bbc?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1478737270239-2f02b77fc618?w=600&auto=format&fit=crop&q=80"
            ),
            summary = "The open-source podcast player that respects your freedom and privacy with zero tracking.",
            description = "AntennaPod gives you instant access to millions of free and paid podcasts, from independent podcasters to large publishing houses such as BBC, NPR and CNN.",
            whatIsNew = "• Auto download episodes over Wi-Fi\n• Chapters with interactive links and artwork\n• Silence skipping and volume boost\n• Sleep timer with shake-to-reset",
            version = "3.4.1",
            releaseDate = "Sep 2025",
            isFeatured = false,
            isEditorChoice = true,
            rank = 10,
            apkDownloadUrl = "https://github.com/AntennaPod/AntennaPod/releases/download/3.4.1/AntennaPod_3.4.1.apk",
            tags = listOf("Podcasts", "Audio", "Privacy", "Open Source", "RSS"),
            ratingBreakdown = RatingBreakdown(0.82f, 0.12f, 0.04f, 0.01f, 0.01f)
        ),
        AppItem(
            id = "chess_tactics_master",
            packageName = "com.boardgames.chesstactics",
            title = "Chess Tactics & Grandmaster AI",
            developer = "DeepCheckmate Games",
            developerWebsite = "https://chessmaster.app",
            category = AppCategory.GAMES,
            rating = 4.9f,
            reviewsCount = "420K",
            downloadsCount = "20M+",
            sizeDisplay = "48 MB",
            sizeBytes = 50_331_648L,
            iconUrl = "https://images.unsplash.com/photo-1529699211952-734e80c4d42b?w=200&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1560174038-da43ac74f01b?w=800&auto=format&fit=crop&q=80",
            screenshots = listOf(
                "https://images.unsplash.com/photo-1529699211952-734e80c4d42b?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1560174038-da43ac74f01b?w=600&auto=format&fit=crop&q=80"
            ),
            summary = "Solve 50,000+ interactive chess puzzles and challenge Stockfish AI engines.",
            description = "Train your tactical vision with daily chess puzzles, opening book explorers, endgame tablebases, and real-time Elo rating progression. Play online against grandmasters worldwide.",
            whatIsNew = "• Neural engine Stockfish 17 evaluation\n• Blunder analysis with visual heatmaps\n• Daily puzzle streak rewards\n• Offline puzzle packs",
            version = "5.0.2",
            releaseDate = "Aug 2025",
            containsAds = true,
            isFeatured = false,
            isEditorChoice = true,
            rank = 11,
            apkDownloadUrl = "https://raw.githubusercontent.com/apphub-demo/assets/main/chesstactics.apk",
            tags = listOf("Chess", "Strategy", "Brain Training", "Puzzles", "Board Games"),
            ratingBreakdown = RatingBreakdown(0.87f, 0.09f, 0.02f, 0.01f, 0.01f)
        ),
        AppItem(
            id = "fitpulse_health",
            packageName = "com.health.fitpulse",
            title = "FitPulse: Workout & Calorie Tracker",
            developer = "PulseFit Technologies",
            developerWebsite = "https://fitpulse.health",
            category = AppCategory.TOOLS,
            rating = 4.8f,
            reviewsCount = "310K",
            downloadsCount = "18M+",
            sizeDisplay = "36 MB",
            sizeBytes = 37_748_736L,
            iconUrl = "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?w=200&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1538805060514-97d9cc17730c?w=800&auto=format&fit=crop&q=80",
            screenshots = listOf(
                "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1538805060514-97d9cc17730c?w=600&auto=format&fit=crop&q=80"
            ),
            summary = "Personalized fitness routines, macro nutrition counter, and body composition analytics.",
            description = "Achieve your fitness goals with customized strength training programs, HIIT interval timers, barcode food scanner, water hydration tracker, and wearable sync.",
            whatIsNew = "• AI-driven meal plan adjustments\n• Rest interval vibration cues\n• Dark theme graphical charts upgrade\n• Health Connect sync integration",
            version = "3.1.5",
            releaseDate = "Sep 2025",
            inAppPurchases = true,
            isFeatured = false,
            isEditorChoice = false,
            rank = 12,
            apkDownloadUrl = "https://raw.githubusercontent.com/apphub-demo/assets/main/fitpulse.apk",
            tags = listOf("Fitness", "Workout", "Health", "Nutrition", "Gym"),
            ratingBreakdown = RatingBreakdown(0.81f, 0.12f, 0.04f, 0.02f, 0.01f)
        )
    )

    // Predefined baseline reviews
    private val defaultReviews: List<ReviewItem> = listOf(
        ReviewItem(
            id = "rev_1",
            appId = "vlc_player",
            userName = "Marcus Chen",
            userAvatarInitial = "M",
            rating = 5,
            date = "August 24, 2025",
            comment = "Easily the cleanest media player on Android. Plays all my 4K video files smoothly without stutter, and no annoying ads ever.",
            helpfulCount = 142
        ),
        ReviewItem(
            id = "rev_2",
            appId = "vlc_player",
            userName = "Sarah Jenkins",
            userAvatarInitial = "S",
            rating = 5,
            date = "August 12, 2025",
            comment = "Subtitle search and sync works like magic. Love the audio equalizer too!",
            helpfulCount = 89
        ),
        ReviewItem(
            id = "rev_3",
            appId = "cyber_sprint_3d",
            userName = "Alex Ryder",
            userAvatarInitial = "A",
            rating = 5,
            date = "September 1, 2025",
            comment = "Graphics are insane on 120Hz display! The soundtrack keeps the adrenaline pumping.",
            helpfulCount = 230
        ),
        ReviewItem(
            id = "rev_4",
            appId = "taskflow_pro",
            userName = "Elena Rostova",
            userAvatarInitial = "E",
            rating = 5,
            date = "August 29, 2025",
            comment = "Replaced 3 different productivity apps for me. The widgets and Pomodoro timer are top notch.",
            helpfulCount = 76
        ),
        ReviewItem(
            id = "rev_5",
            appId = "retro_music",
            userName = "David K.",
            userAvatarInitial = "D",
            rating = 5,
            date = "July 19, 2025",
            comment = "Best looking music player with Material You design. Offline FLAC support is flawless.",
            helpfulCount = 104
        )
    )

    fun getAllApps(): List<AppItem> = appsCatalog

    fun getAppById(id: String): AppItem? = appsCatalog.find { it.id == id }

    fun getFeaturedApps(): List<AppItem> = appsCatalog.filter { it.isFeatured }

    fun getEditorChoiceApps(): List<AppItem> = appsCatalog.filter { it.isEditorChoice }

    fun getTopCharts(category: AppCategory? = null): List<AppItem> {
        val filtered = if (category != null && category != AppCategory.FOR_YOU && category != AppCategory.TOP_CHARTS) {
            appsCatalog.filter { it.category == category }
        } else {
            appsCatalog
        }
        return filtered.sortedBy { it.rank }
    }

    fun getAppsByCategory(category: AppCategory): List<AppItem> {
        return appsCatalog.filter { it.category == category }
    }

    fun searchApps(
        query: String,
        category: AppCategory? = null,
        minRating: Float = 0f
    ): List<AppItem> {
        val cleanQuery = query.trim().lowercase()
        return appsCatalog.filter { app ->
            val matchesQuery = cleanQuery.isEmpty() ||
                    app.title.lowercase().contains(cleanQuery) ||
                    app.developer.lowercase().contains(cleanQuery) ||
                    app.summary.lowercase().contains(cleanQuery) ||
                    app.tags.any { it.lowercase().contains(cleanQuery) }

            val matchesCategory = category == null ||
                    category == AppCategory.FOR_YOU ||
                    category == AppCategory.TOP_CHARTS ||
                    app.category == category

            val matchesRating = app.rating >= minRating

            matchesQuery && matchesCategory && matchesRating
        }
    }

    fun getSimilarApps(appId: String): List<AppItem> {
        val current = getAppById(appId) ?: return appsCatalog.take(4)
        return appsCatalog
            .filter { it.id != appId && (it.category == current.category || it.tags.any { tag -> current.tags.contains(tag) }) }
            .take(6)
            .ifEmpty { appsCatalog.filter { it.id != appId }.take(4) }
    }

    // Wishlist Flow
    val allWishlistAppIds: Flow<Set<String>> = wishlistDao.getAllWishlist().map { list ->
        list.map { it.appId }.toSet()
    }

    fun isWishlisted(appId: String): Flow<Boolean> = wishlistDao.isWishlisted(appId)

    suspend fun toggleWishlist(appId: String) {
        val isSaved = appsCatalog.any { it.id == appId }
        if (isSaved) {
            val entities = wishlistDao.getAllWishlist()
            // We can check if it exists or insert/delete
            wishlistDao.addToWishlist(WishlistEntity(appId = appId))
        }
    }

    suspend fun removeFromWishlist(appId: String) {
        wishlistDao.removeFromWishlist(appId)
    }

    suspend fun addToWishlist(appId: String) {
        wishlistDao.addToWishlist(WishlistEntity(appId = appId))
    }

    fun getWishlistApps(): Flow<List<AppItem>> {
        return allWishlistAppIds.map { idSet ->
            appsCatalog.filter { it.id in idSet }
        }
    }

    // Reviews Flow: Combine pre-seeded reviews + User submitted reviews from Room DB
    fun getReviewsForApp(appId: String): Flow<List<ReviewItem>> {
        val staticReviews = defaultReviews.filter { it.appId == appId }
        return userReviewDao.getReviewsForApp(appId).map { dbReviews ->
            val userReviewsMapped = dbReviews.map { dbItem ->
                ReviewItem(
                    id = "user_rev_${dbItem.id}",
                    appId = dbItem.appId,
                    userName = dbItem.userName,
                    userAvatarInitial = dbItem.userName.take(1).uppercase().ifEmpty { "U" },
                    rating = dbItem.rating,
                    date = dbItem.date,
                    comment = dbItem.comment,
                    helpfulCount = 0,
                    isUserSubmitted = true
                )
            }
            userReviewsMapped + staticReviews
        }
    }

    suspend fun submitReview(appId: String, userName: String, rating: Int, comment: String) {
        val entity = UserReviewEntity(
            appId = appId,
            userName = userName.ifBlank { "AppHub User" },
            rating = rating,
            comment = comment,
            date = "Today"
        )
        userReviewDao.insertReview(entity)
    }
}
