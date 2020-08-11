package com.gabriel.data.repos

import androidx.lifecycle.LiveData
import com.gabriel.data.datasources.defs.APODDataSourceDef
import com.gabriel.data.datasources.impl.local.APODLocalDataSource
import com.gabriel.data.datasources.impl.remote.APODRemoteDataSource
import com.gabriel.data.models.APOD
import javax.inject.Inject

class ApodRepo @Inject constructor() : APODDataSourceDef {

    @Inject
    lateinit var apodRemoteDs: APODRemoteDataSource

    @Inject
    lateinit var apodLocalDs: APODLocalDataSource

    override suspend fun fetchAstronomyPictureOfTheDay(): APOD? {
        return apodLocalDs.fetchAstronomyPictureOfTheDay()
            ?: apodRemoteDs.fetchAstronomyPictureOfTheDay()?.also {
                apodLocalDs.saveApodsToDb(it)
            }
    }

    override suspend fun fetchAstronomyPictures(fromDate: String, endDate: String): List<APOD>? {
        return apodRemoteDs.fetchAstronomyPictures(fromDate, endDate)?.also {
            apodLocalDs.saveApodsToDb(*it.toTypedArray())
        }
    }

    override fun fetchAstronomyPicturesLive(): LiveData<List<APOD>> {
        return apodLocalDs.fetchAstronomyPicturesLive()
    }

    override suspend fun fetchAstronomyPictureByDate(date: String): APOD? {
        return apodLocalDs.fetchAstronomyPictureByDate(date)
            ?: apodRemoteDs.fetchAstronomyPictureByDate(date)?.also {
                apodLocalDs.saveApodsToDb(it)
            }
    }
}