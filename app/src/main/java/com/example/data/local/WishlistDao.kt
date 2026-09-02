package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Query("SELECT * FROM wishlist ORDER BY addedDate DESC")
    fun getAllWishlist(): Flow<List<WishlistEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE appId = :appId)")
    fun isWishlisted(appId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(item: WishlistEntity)

    @Query("DELETE FROM wishlist WHERE appId = :appId")
    suspend fun removeFromWishlist(appId: String)
}
