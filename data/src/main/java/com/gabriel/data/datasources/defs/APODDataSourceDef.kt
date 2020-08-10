package com.gabriel.data.datasources.defs

import com.gabriel.data.models.APOD

internal interface APODDataSourceDef {

    suspend fun fetchAstronomyPictureOfTheDay(): APOD?

    suspend fun fetchAstronomyPictures(fromDate: String, endDate: String): List<APOD>?

    suspend fun fetchAstronomyPictureByDate(date: String): APOD?

    suspend fun saveApodsToDb(vararg apod: APOD) {
        throw NoClassDefFoundError()
    }

    suspend fun deleteApods(vararg apod: APOD) {
        throw NoClassDefFoundError()
    }
}