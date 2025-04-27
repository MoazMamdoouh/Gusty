package com.example.gusty.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.gusty.data.local.alarm.AlarmDao
import com.example.gusty.data.local.favorite.FavoriteDao
import com.example.gusty.data.local.favorite.FavoriteEntity
import com.example.gusty.data.local.home.HomeDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class GustyLocalDataSourceImplTest {
    private lateinit var dataBase: FavoriteDataBase
    private lateinit var dao: FavoriteDao
    private lateinit var alarmDao: AlarmDao
    private lateinit var homeDao: HomeDao
    private lateinit var gustyLocalDataSourceImpl: GustyLocalDataSourceImpl

    @Before
    fun setup() {
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FavoriteDataBase::class.java
        )
            .allowMainThreadQueries()
            .build()
        dao = dataBase.getProductsDao()
        alarmDao = dataBase.getAlarmDao()
        homeDao = dataBase.getHomeDao()
        gustyLocalDataSourceImpl = GustyLocalDataSourceImpl.getInstance(dao , alarmDao ,homeDao )
    }
    @Test
    fun insertItemToFavorite_retrieveSuccess() = runTest{
        val favoriteEntity = FavoriteEntity(
            1 , 0.0 , 0.0 , "moaz" , "mamdouh"
        )
        gustyLocalDataSourceImpl.insertItemToFavorite(favoriteEntity)

        val result = gustyLocalDataSourceImpl.getListOfFavoriteItems().first()[0]

        assertThat(result , `is`(favoriteEntity))
    }
    @Test
    fun deleteItemToFavorite_retrieveSuccess() = runTest{
        val favoriteEntity = FavoriteEntity(
            1 , 0.0 , 0.0 , "moaz" , "mamdouh"
        )

        val itemList : MutableList<FavoriteEntity> = mutableListOf()
        itemList.add(favoriteEntity)

        gustyLocalDataSourceImpl.insertItemToFavorite(favoriteEntity)

        itemList.remove(favoriteEntity)

        gustyLocalDataSourceImpl.deleteLocationFromFavorite(favoriteEntity)
        val result = gustyLocalDataSourceImpl.getListOfFavoriteItems().first()

        assertThat(result , `is`(listOf()))
    }

    @After
    fun finalize(){
        dataBase.close()
    }
}