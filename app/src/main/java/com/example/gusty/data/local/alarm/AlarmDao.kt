package com.example.gusty.data.local.alarm

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gusty.data.local.favorite.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToAlarm(alarmEntity: AlarmEntity) : Long

    @Query("SELECT * FROM alarm_table")
    fun getAllAlarms() : Flow<List<AlarmEntity>>

    @Delete
    suspend fun deleteAlarmFromDataBase(alarmEntity: AlarmEntity) : Int

}