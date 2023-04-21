package com.kal.brawlstatz2.retrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder().baseUrl("https://api.brawlapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val apiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}