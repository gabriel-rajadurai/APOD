package com.gabriel.data.impl

import com.gabriel.data.api.APODRetrofitHelper
import com.gabriel.data.defs.APODDataSourceDef
import com.gabriel.data.models.APOD
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class APODDataSource : APODDataSourceDef {
    private val apodNetworkService by lazy {
        APODRetrofitHelper().getApodNetworkService()
    }

    override suspend fun fetchAstronomyPictureOfTheDay(): APOD? {
        return suspendCoroutine {
            apodNetworkService.fetchAstronomyPictureOfTheDay()
                .enqueue(object : Callback<APOD> {
                    override fun onFailure(call: Call<APOD>, t: Throwable) {
                        it.resumeWithException(t)
                    }

                    override fun onResponse(call: Call<APOD>, response: Response<APOD>) {
                        if (response.isSuccessful)
                            it.resume(response.body())
                        else
                            it.resume(null)
                    }
                })
        }
    }

    override suspend fun fetchAstronomyPictures(fromDate: String, endDate: String): List<APOD>? {
        return suspendCoroutine {
            apodNetworkService.fetchAstronomyPictures(fromDate, endDate)
                .enqueue(object : Callback<List<APOD>> {
                    override fun onFailure(call: Call<List<APOD>>, t: Throwable) {
                        it.resumeWithException(t)
                    }

                    override fun onResponse(
                        call: Call<List<APOD>>,
                        response: Response<List<APOD>>
                    ) {
                        if (response.isSuccessful)
                            it.resume(response.body())
                        else
                            it.resume(null)
                    }

                })
        }
    }

    override suspend fun fetchAstronomyPictureByDate(date: String): APOD? {
        return suspendCoroutine {
            apodNetworkService.fetchAstronomyPictureByDate(date)
                .enqueue(object : Callback<APOD> {
                    override fun onFailure(call: Call<APOD>, t: Throwable) {
                        it.resumeWithException(t)
                    }

                    override fun onResponse(call: Call<APOD>, response: Response<APOD>) {
                        if (response.isSuccessful)
                            it.resume(response.body())
                        else
                            it.resume(null)
                    }

                })
        }
    }
}