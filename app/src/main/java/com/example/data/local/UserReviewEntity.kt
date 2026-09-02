package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_reviews")
data class UserReviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val appId: String,
    val userName: String,
    val rating: Int,
    val comment: String,
    val date: String,
    val timestamp: Long = System.currentTimeMillis()
)
