package com.example.gusty.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.gusty.data.local.favorite.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insertItemToFavorite(favoriteEntity: FavoriteEntity):Long

    @Query("SELECT * FROM weather_favorite_items")
    fun getAllFavoriteItems() : Flow<List<FavoriteEntity>>
    @Delete
    suspend fun deleteLocationFromFavorite(favoriteEntity: FavoriteEntity) : Int

}