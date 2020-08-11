package com.gabriel.data.api

import com.gabriel.data.models.APOD
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APODNetworkService {

    @GET("apod")
    fun fetchAstronomyPictureOfTheDay(): Call<APOD> // TODO Issue in API -> Returns null during early hours of the day

    @GET("apod")
    fun fetchAstronomyPictures(
        @Query("start_date") fromDate: String,
        @Query("end_date") endDate: String
    ): Call<List<APOD>>

    @GET("apod")
    fun fetchAstronomyPictureByDate(@Query("date") date: String): Call<APOD>
}