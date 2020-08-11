package com.gabriel.data.datasources.impl.remote

import com.gabriel.data.api.APODNetworkService
import com.gabriel.data.api.APODRetrofitHelper
import com.gabriel.data.datasources.defs.APODDataSourceDef
import com.gabriel.data.models.APOD
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class APODRemoteDataSource @Inject constructor(
    private val apodNetworkService: APODNetworkService
) : APODDataSourceDef {

    override suspend fun fetchAstronomyPictureOfTheDay(): APOD? {
        return suspendCoroutine {

            apodNetworkService.fetchAstronomyPictureByDate(
                ZonedDateTime.now(ZoneOffset.MIN).format(
                    DateTimeFormatter.ofPattern(APOD.DATE_FORMAT)
                )
            ).enqueue(object : Callback<APOD> {
                override fun onFailure(call: Call<APOD>, t: Throwable) {
                    t.printStackTrace()
                    it.resume(null)
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
                        t.printStackTrace()
                        it.resume(null)
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
                        t.printStackTrace()
                        it.resume(null)
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