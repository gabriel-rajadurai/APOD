package com.gabriel.data.datasources.defs

import androidx.lifecycle.LiveData
import com.gabriel.data.models.APOD

internal interface APODDataSourceDef {

    suspend fun fetchAstronomyPictureOfTheDay(): APOD?

    suspend fun fetchAstronomyPictures(fromDate: String, endDate: String): List<APOD>? {
        throw NoClassDefFoundError()
    }

    fun fetchAstronomyPicturesLive(): LiveData<List<APOD>> {
        throw NoClassDefFoundError()
    }

    suspend fun fetchAstronomyPictureByDate(date: String): APOD?

    suspend fun saveApodsToDb(vararg apod: APOD) {
        throw NoClassDefFoundError()
    }

    suspend fun deleteApods(vararg apod: APOD) {
        throw NoClassDefFoundError()
    }
}