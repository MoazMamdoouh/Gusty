package com.example.gusty.favorite

import com.example.gusty.data.local.favorite.FavoriteEntity
import com.example.gusty.data.repo.GustyRepoImpl
import com.example.gusty.utilities.UiStateResult
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.Before
import org.junit.Test

class FavoriteViewModelTest {

    lateinit var viewModel: FavoriteViewModel
    lateinit var stubRepo: GustyRepoImpl

    val favoriteEntity = FavoriteEntity(
        id = 1,
        lat = 0.0,
        lon = 0.0,
        countryName = "Egypt",
        cityName = "Cairo",
    )

    @Before
    fun setUp() {
        stubRepo = mockk()
        viewModel = FavoriteViewModel(stubRepo)
    }

    @Test
    fun insertFavEntity_insertionSuccess() = runTest {
        viewModel.insertToFavorite(favoriteEntity)
        val result = viewModel.listOfFavorite.value
        if (result is UiStateResult.Success) {
            assertThat(result.response, `is`(favoriteEntity))
        }
    }

    @Test
    fun getCurrentWeather_currentWeather() = runTest {
        viewModel.getCurrentWeatherForFavorite(0.0, 0.0, "ay 7aga", "ay 7aga")
        val result = viewModel.currentWeather.value
        if (result is UiStateResult.Success) {
            assertThat(result.response, not(nullValue()))
        }
    }
}