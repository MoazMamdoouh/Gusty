package com.example.gusty.alarm

import com.example.gusty.data.local.alarm.AlarmEntity

interface AlarmScheduler {
    fun scheduler(item : AlarmEntity)
    fun cancel(item : AlarmEntity)
}