package com.example.gusty.alarm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gusty.data.local.alarm.AlarmEntity
import com.example.gusty.data.model.curren_weather_dto.CurrentWeatherDto
import com.example.gusty.data.repo.GustyRepo
import com.example.gusty.utilities.UiStateResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.TimeZone


class AlarmViewModel(private val repo: GustyRepo) : ViewModel() {

    private val _currentWeather: MutableStateFlow<UiStateResult<CurrentWeatherDto>> =
        MutableStateFlow(UiStateResult.Loading)
    val currentWeather = _currentWeather.asStateFlow()

    private val _listOfAlarms: MutableStateFlow<UiStateResult<List<AlarmEntity>>> =
        MutableStateFlow(UiStateResult.Loading)
    val listOfAlarms = _listOfAlarms.asStateFlow()


    fun getCurrentWeather(latitude: Double, longitude: Double , unit : String ) {
        viewModelScope.launch {
            try {
                    repo.getCurrentWeather(latitude, longitude , unit)
                    .catch { _currentWeather.emit(UiStateResult.Failure(Throwable())) }
                    .collect {
                        _currentWeather.emit(UiStateResult.Success(it))
                    }
            } catch (e: Exception) {
                _currentWeather.emit(UiStateResult.Failure(e))
            }
        }
    }

    fun insertAlarm(
        androidAlarmManager: AndroidAlarmManager,
        id: Int,
        day: String,
        time: String,
        place: String,
        duration: Long
    ) {
        viewModelScope.launch {
            val alarmEntity = AlarmEntity(
                id,
                day,
                time,
                place
            )
            try {
                    repo.insertAlarmToDataBase(alarmEntity)
                    androidAlarmManager.scheduler(alarmEntity.id, duration)
            } catch (e: Exception) {
                Log.i("TAG", "insertAlarm view model error : ${e.message} ")
            }
        }
    }

    fun calculateAlarmDuration(min: Int, hour: Int, stamp: Long): Long {
        val localDateTime =
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(stamp), TimeZone
                    .getDefault().toZoneId()
            )
        val day = localDateTime.dayOfMonth
        val month = localDateTime.monthValue
        val year = localDateTime.year
        return getDuration(min = min, hour = hour, day = day, month = month, year = year)
    }

    private fun getDuration(min: Int, hour: Int, day: Int, month: Int, year: Int): Long {
        val localDateTime = LocalDateTime.of(year, month, day, hour, min)
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    }

    fun getAllAlarmsFromDataBase() {
        viewModelScope.launch {
            try {
                repo.getAllAlarmsFromDataBase()
                    .catch {
                        Log.i("TAG", "getAllAlarmsFromDataBase catch throwable $it ")
                        _listOfAlarms.emit(UiStateResult.Failure(Throwable()))
                    }
                    .collect {
                        _listOfAlarms.emit(UiStateResult.Success(it))
                    }
            }catch (e : Exception){
                Log.i("TAG", "getAllAlarmsFromDataBase view model ${e.message} ")
                _listOfAlarms.emit(UiStateResult.Failure(e))
            }
        }
    }

    fun deleteAlarmFromDataBase(alarmEntity: AlarmEntity){
        viewModelScope.launch {
            try {
                repo.deleteAlarmFromDataBase(alarmEntity)
            }catch (e : Exception){
                Log.i("TAG", "deleteAlarmFromDataBase view model error in deleting ${e.message} ")
            }
        }
    }
}

class AlarmFactory(val repo: GustyRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlarmViewModel(repo) as T
    }
}