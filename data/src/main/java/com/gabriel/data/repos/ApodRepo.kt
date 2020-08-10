package com.gabriel.data.repos

import com.gabriel.data.defs.APODDataSourceDef
import com.gabriel.data.impl.APODDataSource
import com.gabriel.data.models.APOD

class ApodRepo : APODDataSourceDef {

    private val apodDs by lazy { APODDataSource() }

    override suspend fun fetchAstronomyPictureOfTheDay(): APOD? {
        return apodDs.fetchAstronomyPictureOfTheDay()
    }

    override suspend fun fetchAstronomyPictures(fromDate: String, endDate: String): List<APOD>? {
        return apodDs.fetchAstronomyPictures(fromDate, endDate)
    }

    override suspend fun fetchAstronomyPictureByDate(date: String): APOD? {
        return apodDs.fetchAstronomyPictureByDate(date)
    }
}