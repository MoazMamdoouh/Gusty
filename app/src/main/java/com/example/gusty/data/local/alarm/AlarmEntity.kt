package com.example.gusty.data.local.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "alarm_table")
data class AlarmEntity(
    @PrimaryKey
    val id : Int ,
    val datePicked : String ,
    val timePicked : String ,
    val place : String ,
)
