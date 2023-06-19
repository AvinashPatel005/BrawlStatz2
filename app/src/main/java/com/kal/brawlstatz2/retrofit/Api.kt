package com.kal.brawlstatz2.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface Api {
    @GET
    suspend fun getPageResponse(@Url url:String) : Response<String>
}