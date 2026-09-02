package com.example.data.model

data class ReviewItem(
    val id: String,
    val appId: String,
    val userName: String,
    val userAvatarUrl: String = "",
    val userAvatarInitial: String = "U",
    val rating: Int,
    val date: String,
    val comment: String,
    val helpfulCount: Int = 0,
    val isUserSubmitted: Boolean = false
)
