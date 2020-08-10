package com.gabriel.data.repos

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
        return apodLocalDs.fetchAstronomyPictureOfTheDay()?.also {
            apodLocalDs.saveApodsToDb(it)
        } ?: apodRemoteDs.fetchAstronomyPictureOfTheDay()
    }

    override suspend fun fetchAstronomyPictures(fromDate: String, endDate: String): List<APOD>? {
        val apodsLocal = apodLocalDs.fetchAstronomyPictures(fromDate, endDate)
        return if (!apodsLocal.isNullOrEmpty()) {
            apodsLocal
        } else {
            apodRemoteDs.fetchAstronomyPictures(fromDate, endDate)?.also {
                apodLocalDs.saveApodsToDb(*it.toTypedArray())
            }
        }
    }

    override suspend fun fetchAstronomyPictureByDate(date: String): APOD? {
        return apodLocalDs.fetchAstronomyPictureByDate(date)
            ?: apodRemoteDs.fetchAstronomyPictureByDate(date)
    }
}