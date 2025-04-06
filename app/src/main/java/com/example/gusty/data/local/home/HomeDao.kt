package com.example.gusty.data.local.home

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface HomeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomeScreen(homeEntity: HomeEntity) : Long

    @Query("SELECT * FROM home_table")
    fun getHomeObj() : Flow<HomeEntity>
}