package com.example.gusty.favorite

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gusty.data.local.favorite.FavoriteEntity
import com.example.gusty.data.repo.GustyRepo
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteViewModelTest{
    lateinit var viewModel: FavoriteViewModel
    lateinit var repo : GustyRepo

    @Before
    fun setUp(){
        repo = DummyRepo()
        viewModel = FavoriteViewModel(repo)
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertFavCity_favoriteItem_string()= runTest {

        val favoriteViewModel = FavoriteEntity(
            11, 0.0 , 0.0 , "moaz" , "mamdouh"
        )

        val messages = mutableListOf<String>()

        val job = launch {
            viewModel.message.collect { messages.add(it) }
        }

        delay(1000)
        viewModel.insertToFavorite(favoriteViewModel)
        delay(1000)
        advanceUntilIdle()
        job.cancel()

        assertEquals("Insertion Success", messages.firstOrNull())
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteFavCity_string()=runTest {

        val favoriteViewModel = FavoriteEntity(
            1 , 0.0 , 0.0 , "moaz" , "mamdouh"
        )
        viewModel.insertToFavorite(favoriteViewModel)

        val messages = mutableListOf<String?>()

        val job = launch {
            viewModel.message.collect { messages.add(it) }
        }

        delay(1000)
        viewModel.deleteLocationFromFavorite(favoriteViewModel)

        advanceUntilIdle()
        job.cancel()

        assertEquals("Deletion Success", messages.first())
    }
}