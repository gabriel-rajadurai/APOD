package com.gabriel.data.defs

import com.gabriel.data.models.APOD

internal interface APODDataSourceDef {

    suspend fun fetchAstronomyPictureOfTheDay(): APOD?

    suspend fun fetchAstronomyPictures(fromDate: String, endDate: String): List<APOD>?
}