package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {
    @Query("SELECT * FROM downloads ORDER BY downloadDate DESC")
    fun getAllDownloads(): Flow<List<DownloadEntity>>

    @Query("SELECT * FROM downloads WHERE appId = :appId LIMIT 1")
    fun getDownloadByAppId(appId: String): Flow<DownloadEntity?>

    @Query("SELECT * FROM downloads WHERE status = 'COMPLETED' ORDER BY downloadDate DESC")
    fun getCompletedDownloads(): Flow<List<DownloadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(download: DownloadEntity)

    @Update
    suspend fun update(download: DownloadEntity)

    @Query("DELETE FROM downloads WHERE appId = :appId")
    suspend fun deleteByAppId(appId: String)

    @Query("DELETE FROM downloads")
    suspend fun clearAll()
}
