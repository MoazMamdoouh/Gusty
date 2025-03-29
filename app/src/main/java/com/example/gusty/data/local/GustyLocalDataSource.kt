package com.example.gusty.data.local

import com.example.gusty.data.remote.Api
import com.example.gusty.data.remote.GustyRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GustyLocalDataSource private constructor(private val dao : FavoriteDao) {

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
    companion object {
        private var INSTANCE: GustyLocalDataSource? = null
        fun getInstance(
            dao: FavoriteDao
        ): GustyLocalDataSource {
            return INSTANCE ?: synchronized(this) {
                val temp = GustyLocalDataSource(dao)
                INSTANCE = temp
                temp
            }
        }
    }
}