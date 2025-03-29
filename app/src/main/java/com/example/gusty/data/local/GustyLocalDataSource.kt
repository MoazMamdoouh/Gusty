package com.example.gusty.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GustyLocalDataSource(private val dao : FavoriteDao) {

     suspend fun insertItemToFavorite(favoriteEntity: FavoriteEntity): Long {
        return dao.insertItemToFavorite(favoriteEntity)
    }
    fun getListOfFavoriteItems():Flow<List<FavoriteEntity>>{
        return try {
            dao.getAllFavoriteItems()
        }catch (e:Exception){
            return flowOf()
        }
    }

    suspend fun deleteLocationFromFavorite(favoriteEntity: FavoriteEntity) : Int {
        return try {
            dao.deleteLocationFromFavorite(favoriteEntity)
        }catch ( e : Exception){
            return 0
        }
    }
}