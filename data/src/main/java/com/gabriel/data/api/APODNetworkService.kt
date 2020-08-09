package com.gabriel.data.api

import com.gabriel.data.models.APOD
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface APODNetworkService {

    @GET("apod")
    fun fetchAstronomyPictureOfTheDay(): Call<APOD>

    @GET("apod")
    fun fetchAstronomyPictures(
        @Query("start_date") fromDate: String,
        @Query("end_date") endDate: String
    ): Call<List<APOD>>
}