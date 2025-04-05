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
import com.example.gusty.home.model.hourly_daily_model.hourlyModel
import com.example.gusty.home.model.hourly_daily_model.mapDailyDtoToModel
import com.example.gusty.home.model.mapDtoToModel
import com.example.gusty.utilities.UiStateResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeViewModel(val repo: GustyRepo) : ViewModel() {

    //current weather
    private val _currentWeather : MutableStateFlow<UiStateResult<CurrentWeatherModel>>
    =  MutableStateFlow(UiStateResult.Loading)
    val currentWeather  = _currentWeather.asStateFlow()

    // hourly weather
    private val _hourlyWeather = MutableLiveData<List<HourlyAndDailyModel>>()
    val hourlyWeather = _hourlyWeather

    // daily weather
    private val _dailyWeather = MutableLiveData<List<HourlyAndDailyModel>>()
    val dailyWeather = _dailyWeather
    fun getCurrentWeather(latitude: Double, longitude: Double , unit : String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val current = repo.getCurrentWeather(latitude , longitude , unit)
                    .map { dto -> dto.mapDtoToModel() }
                    .catch {
                        Log.i("TAG", "home view model failure ${Throwable().message}  ")
                        _currentWeather.emit(UiStateResult.Failure(Throwable())) }
                current.collect {
                    _currentWeather.emit(UiStateResult.Success(it))
                }
            } catch (e: Exception) {
                _currentWeather.emit(UiStateResult.Failure(e))
                Log.i("TAG", "getCurrentWeather: view model error ${e.message}")
            }
        }
    }

    fun getHourlyWeather(latitude: Double, longitude: Double , unit: String) {
        viewModelScope.launch {
            try {
                repo.getDailyAndHourlyWeather(latitude , longitude , unit)
                    .map { daily -> daily.hourlyModel() }
                    .collect {
                        _hourlyWeather.postValue(it)
                    }
            } catch (e: Exception) {
                Log.i("TAG", "getDailyWeather viewModel error : with ${e.message}")
            }
        }
    }
    fun getDailyWeather(latitude: Double, longitude: Double , unit: String) {
        viewModelScope.launch {
            try {
                repo.getDailyAndHourlyWeather(latitude , longitude , unit)
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