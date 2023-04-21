package com.kal.brawlstatz2.retrofit

import com.kal.brawlstatz2.data.events.event
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("events")
    fun getData() : Call<event>
}