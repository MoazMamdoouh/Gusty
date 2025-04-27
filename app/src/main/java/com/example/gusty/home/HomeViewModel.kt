package com.example.gusty.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gusty.data.local.home.HomeEntity
import com.example.gusty.data.repo.GustyRepo
import com.example.gusty.home.model.current_weather_model.CurrentWeatherModel
import com.example.gusty.home.model.current_weather_model.mapDtoToModel
import com.example.gusty.home.model.hourly_daily_model.HourlyAndDailyModel
import com.example.gusty.home.model.hourly_daily_model.hourlyModel
import com.example.gusty.home.model.hourly_daily_model.mapDailyDtoToModel
import com.example.gusty.utilities.UiStateResult
import kotlinx.coroutines.Dispatchers
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
    private val _hourlyWeather : MutableStateFlow<UiStateResult<List<HourlyAndDailyModel>>>
            = MutableStateFlow(UiStateResult.Loading)
    val hourlyWeather = _hourlyWeather.asStateFlow()

    // daily weather
    private val _dailyWeather : MutableStateFlow<UiStateResult<List<HourlyAndDailyModel>>>
            = MutableStateFlow(UiStateResult.Loading)
    val dailyWeather = _dailyWeather.asStateFlow()

    //no connection
    private val _currentWeatherNoInterNet : MutableStateFlow<UiStateResult<CurrentWeatherModel>>
            =  MutableStateFlow(UiStateResult.Loading)
    val currentWeatherNoInterNet  = _currentWeatherNoInterNet.asStateFlow()

    private val _hourlyAndDailyWeatherNoInterNet : MutableStateFlow<UiStateResult<List<HourlyAndDailyModel>>>
            = MutableStateFlow(UiStateResult.Loading)
    val hourlyWeatherNoInterNet = _hourlyAndDailyWeatherNoInterNet.asStateFlow()

    fun getCurrentWeather(latitude: Double, longitude: Double , unit : String , lang : String ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val current = repo.getCurrentWeather(latitude , longitude , unit , lang)
                    .map { dto -> dto.mapDtoToModel() }
                    .catch {
                        Log.i("home", "home view model failure ${Throwable().message}  ")
                        _currentWeather.emit(UiStateResult.Failure(Throwable())) }
                current.collect {
                    _currentWeather.emit(UiStateResult.Success(it))
                }
            } catch (e: Exception) {
                _currentWeather.emit(UiStateResult.Failure(e))
                Log.i("home", "getCurrentWeather: view model error ${e.message}")
            }
        }
    }

    fun getHourlyWeather(latitude: Double, longitude: Double , unit: String) {
        viewModelScope.launch {
            try {
                repo.getDailyAndHourlyWeather(latitude , longitude , unit)
                    .map { daily -> daily.hourlyModel() }
                    .collect {
                        _hourlyWeather.emit(UiStateResult.Success(it))
                    }
            } catch (e: Exception) {
                Log.i("home", "getDailyWeather viewModel error : with ${e.message}")
                _hourlyWeather.emit(UiStateResult.Failure(e))
            }
        }
    }
    fun getDailyWeather(latitude: Double, longitude: Double , unit: String) {
        viewModelScope.launch {
            try {
                repo.getDailyAndHourlyWeather(latitude , longitude , unit)
                    .map { daily -> daily.mapDailyDtoToModel() }
                    .collect {
                        _dailyWeather.emit(UiStateResult.Success(it))
                    }
            } catch (e: Exception) {
                Log.i("home", "getDailyWeather viewModel error : with ${e.message}")
                _dailyWeather.emit(UiStateResult.Failure(e))
            }
        }
    }
    fun insertHomeEntity(homeEntity: HomeEntity){
        viewModelScope.launch {
            try {
                Log.i("fav", "insertHomeEntity success ")
                repo.insertHomeScreen(homeEntity)
            }catch (e : Exception){
                Log.i("fav", "insertHomeEntity error ${e.message} ")
            }
        }
    }
    fun getHomeObj(){
        viewModelScope.launch {
            try {
                repo.getHomeObj()
                    .catch { _currentWeatherNoInterNet.emit(UiStateResult.Failure(Throwable())) }
                    .collect {homeEntity ->
                        _currentWeatherNoInterNet.emit(UiStateResult.Success(homeEntity.currentWeather))
                        _hourlyAndDailyWeatherNoInterNet.emit(UiStateResult.Success(homeEntity.hourlyAndDaily))
                    }
            } catch (e: Exception) {

            }
        }
    }
}


class HomeFactory(val repo: GustyRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repo) as T
    }
}