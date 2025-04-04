package com.example.gusty.alarm

import com.example.gusty.data.local.alarm.AlarmEntity

interface AlarmScheduler {
    fun scheduler(id : Int , time : Long)
    fun cancel(id : Int)
}