package com.gabriel.data.repos

import android.content.Context
import com.gabriel.data.datasources.ApodDatabase
import com.gabriel.data.datasources.defs.APODDataSourceDef
import com.gabriel.data.datasources.impl.local.APODLocalDataSource
import com.gabriel.data.datasources.impl.remote.APODRemoteDataSource
import com.gabriel.data.models.APOD

class ApodRepo(context: Context) : APODDataSourceDef {

    private val apodRemoteDs by lazy { APODRemoteDataSource() }
    private val apodLocalDs by lazy {
        APODLocalDataSource(
            ApodDatabase.getDatabase(context).apodDao()
        )
    }

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