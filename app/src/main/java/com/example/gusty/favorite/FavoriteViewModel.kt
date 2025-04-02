package com.example.gusty.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gusty.data.local.favorite.FavoriteEntity
import com.example.gusty.data.local.favorite.convertCurrentDtoToEntity
import com.example.gusty.data.repo.GustyRepo
import com.example.gusty.utilities.UiStateResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repo: GustyRepo) : ViewModel() {

    private val _currentWeather: MutableStateFlow<UiStateResult<FavoriteEntity>> =
        MutableStateFlow(UiStateResult.Loading)
    val currentWeather = _currentWeather.asStateFlow()

    private val _listOfFavorite: MutableStateFlow<UiStateResult<List<FavoriteEntity>>> =
        MutableStateFlow(UiStateResult.Loading)
    val listOfFavorite  = _listOfFavorite.asStateFlow()

    fun getCurrentWeatherForFavorite(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                repo.getCurrentWeather(latitude, longitude)
                    .map { dto -> dto.convertCurrentDtoToEntity() }
                    .catch { throwable ->
                        _currentWeather.emit(UiStateResult.Failure(throwable))
                    }
                    .collect { entity ->
                        Log.i("TAG", "view model getCurrentWeatherForFavorite success ")
                        _currentWeather.emit(UiStateResult.Success(entity))
                        //insertToFavorite(entity)
                    }
            } catch (e: Exception) {
                _currentWeather.emit(UiStateResult.Failure(e))
            }
        }
    }

    fun insertToFavorite(favoriteEntity: FavoriteEntity) {
        viewModelScope.launch {
            try {
                repo.insertItemToFavorite(favoriteEntity)
                Log.i("TAG", "view model insertion success ")
            } catch (e: Exception) {
                Log.i("TAG", "view model insertion error ${e.message} ")
            }
        }
    }

    fun getListOfFavoriteItems(){
        viewModelScope.launch {
            try {
                repo.getListOfFavoriteItems()
                    .catch { throwable ->
                        _currentWeather.emit(UiStateResult.Failure(throwable))
                    }.collect {
                        _listOfFavorite.emit(UiStateResult.Success(it))
                    }
            }catch ( e : Exception){
                _currentWeather.emit(UiStateResult.Failure(e))
            }
        }
    }

    fun deleteLocationFromFavorite(favoriteEntity: FavoriteEntity){
        viewModelScope.launch {
            try {
                repo.deleteLocationFromFavorite(favoriteEntity)
            }catch (e : Exception){
                Log.i("TAG", "deleteLocationFromFavorite error ${e.message} ")
            }
        }
    }
}

class FavoriteFactory(val repo: GustyRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteViewModel(repo) as T
    }
}