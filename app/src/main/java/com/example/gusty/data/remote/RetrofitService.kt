package com.example.gusty.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    private  val  BASE_URL = "https://api.openweathermap.org/data/2.5/"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api : Api = retrofit.create(Api::class.java)
}