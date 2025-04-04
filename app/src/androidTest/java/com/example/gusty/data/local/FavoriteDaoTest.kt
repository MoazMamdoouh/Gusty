package com.example.gusty.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.gusty.data.local.favorite.FavoriteDao
import com.example.gusty.data.local.favorite.FavoriteEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@SmallTest
@RunWith(AndroidJUnit4::class)
class FavoriteDaoTest {
    private lateinit var dataBase: FavoriteDataBase
    private lateinit var dao: FavoriteDao

    @Before
    fun setup() {
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FavoriteDataBase::class.java
        )
            .allowMainThreadQueries()
            .build()
        dao = dataBase.getProductsDao()
    }

    @Test
    fun insertItemToFavorite_retrieveSuccess() = runTest{
        val favoriteEntity = FavoriteEntity(
            1 , 0.0 , 0.0 , "moaz" , "mamdouh"
        )
        dao.insertItemToFavorite(favoriteEntity)

        val result = dao.getAllFavoriteItems().first()[0]

        assertThat(result , `is`(favoriteEntity))
    }
    @Test
    fun deleteItemToFavorite_retrieveSuccess() = runTest{
        val favoriteEntity = FavoriteEntity(
            1 , 0.0 , 0.0 , "moaz" , "mamdouh"
        )

        val itemList : MutableList<FavoriteEntity> = mutableListOf()
        itemList.add(favoriteEntity)

        dao.insertItemToFavorite(favoriteEntity)

        itemList.remove(favoriteEntity)

        dao.deleteLocationFromFavorite(favoriteEntity)
        val result = dao.getAllFavoriteItems().first()

        assertThat(result , `is`(listOf()))
    }

    @After
    fun finalize(){
        dataBase.close()
    }
}