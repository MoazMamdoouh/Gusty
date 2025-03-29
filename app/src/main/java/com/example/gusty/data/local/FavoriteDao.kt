package com.example.gusty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insertItemToFavorite(favoriteEntity: FavoriteEntity):Long

    @Query("SELECT * FROM weather_favorite_items")
    fun getAllFavoriteItems() : Flow<List<FavoriteEntity>>

}