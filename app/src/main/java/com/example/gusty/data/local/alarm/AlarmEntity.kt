package com.example.gusty.data.local.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "alarm_table")
data class AlarmEntity(
    @PrimaryKey
    val id : Int ,
    val startDuration : String ,
    val place : String ,
)
