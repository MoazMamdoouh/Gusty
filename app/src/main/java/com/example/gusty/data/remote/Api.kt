package com.example.gusty.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface Api {


    @GET("daily")
    suspend fun getDailyWeatherInfo(
        // take the lan
        // take the lon
        // take the API_KEY
    ) : Response<Any>
}