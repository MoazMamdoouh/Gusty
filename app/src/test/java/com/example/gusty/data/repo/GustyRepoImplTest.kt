package com.example.gusty.data.repo

import com.example.gusty.data.local.favorite.FavoriteEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test


class GustyRepoImplTest{
    lateinit var fakeLocalDataSource: FakeLocalDataSource
    lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    lateinit var gustyRepoImpl : GustyRepoImpl

    var list = mutableListOf<FavoriteEntity>(
        FavoriteEntity(
            1 , 0.0 , 0.0 , "moaz" , "mamdouh"
        ) ,
        FavoriteEntity(
            2 , 0.0 , 0.0 , "moaz" , "mamdouh"
        ) ,
        FavoriteEntity(
            3 , 0.0 , 0.0 , "moaz" , "mamdouh"
        )
    )
    @Before
    fun setUp(){
        fakeLocalDataSource = FakeLocalDataSource(list)
        fakeRemoteDataSource = FakeRemoteDataSource()
        gustyRepoImpl = GustyRepoImpl.getInstance(fakeRemoteDataSource , fakeLocalDataSource)
    }
    @Test
    fun insertItemToFavorite_retrieveSuccess() = runTest{
        val favoriteEntity = FavoriteEntity(
            4 , 0.0 , 0.0 , "moaz" , "mamdouh"
        )
        gustyRepoImpl.insertItemToFavorite(favoriteEntity)

        list.add(favoriteEntity)

        val result = gustyRepoImpl.getListOfFavoriteItems().first()
        assertThat(list as List<FavoriteEntity> , `is`(result))
    }
    @Test
    fun deleteItemToFavorite_retrieveSuccess() = runTest{
        val favoriteEntity = FavoriteEntity(
            1 , 0.0 , 0.0 , "moaz" , "mamdouh"
        )

        val itemList : MutableList<FavoriteEntity> = mutableListOf()
        itemList.add(favoriteEntity)

        gustyRepoImpl.insertItemToFavorite(favoriteEntity)
        gustyRepoImpl.deleteLocationFromFavorite(favoriteEntity)


        val result = gustyRepoImpl.getListOfFavoriteItems().first()

        assertThat(result , `is`(list))
    }

}