package com.example.gusty.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gusty.data.repo.GustyRepo
import com.example.gusty.home.model.CurrentWeatherModel
import com.example.gusty.home.model.hourly_daily_model.HourlyAndDailyModel
import com.example.gusty.home.model.hourly_daily_model.convertUnixToHour
import com.example.gusty.home.model.hourly_daily_model.hourlyModel
import com.example.gusty.home.model.hourly_daily_model.mapDailyDtoToModel
import com.example.gusty.home.model.mapDtoToModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeViewModel(val repo: GustyRepo) : ViewModel() {

    //current weather
    private val _currentWeather = MutableLiveData<CurrentWeatherModel>()
    val currentWeather: LiveData<CurrentWeatherModel> = _currentWeather

    // hourly weather
    private val _hourlyWeather = MutableLiveData<List<HourlyAndDailyModel>>()
    val hourlyWeather = _hourlyWeather
    // daily weather
    private val _dailyWeather = MutableLiveData<List<HourlyAndDailyModel>>()
    val dailyWeather = _dailyWeather
    fun getCurrentWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val current = repo.getCurrentWeather()
                    .map { dto -> dto.mapDtoToModel() }
                current.collect {
                    _currentWeather.postValue(it)
                }
            } catch (e: Exception) {
                Log.i("TAG", "getCurrentWeather: view model error ${e.message}")
            }
        }
    }

    fun getHourlyWeather() {
        viewModelScope.launch {
            try {
                repo.getDailyAndHourlyWeather()
                    .map { daily -> daily.hourlyModel() }
                    .collect {
                        _hourlyWeather.postValue(it)
                    }
            } catch (e: Exception) {
                Log.i("TAG", "getDailyWeather viewModel error : with ${e.message}")
            }
        }
    }
    fun getDailyWeather() {
        viewModelScope.launch {
            try {
                repo.getDailyAndHourlyWeather()
                    .map { daily -> daily.mapDailyDtoToModel() }
                    .collect {
                        _dailyWeather.postValue(it)
                    }
            } catch (e: Exception) {
                Log.i("TAG", "getDailyWeather viewModel error : with ${e.message}")
            }
        }
    }
}


class HomeFactory(val repo: GustyRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repo) as T
    }
}