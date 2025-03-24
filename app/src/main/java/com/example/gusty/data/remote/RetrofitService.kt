package com.example.gusty.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    val API_KEY =  "b753d8e898af2c2c8235d35152cf5139"
    private final val  BASE_URL = "api.openweathermap.org/data/2.5/forecast/"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api : Api = retrofit.create(Api::class.java)
}