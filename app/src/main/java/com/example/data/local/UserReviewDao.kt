package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserReviewDao {
    @Query("SELECT * FROM user_reviews WHERE appId = :appId ORDER BY timestamp DESC")
    fun getReviewsForApp(appId: String): Flow<List<UserReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: UserReviewEntity)

    @Query("SELECT COUNT(*) FROM user_reviews WHERE appId = :appId")
    suspend fun getReviewCount(appId: String): Int
}
