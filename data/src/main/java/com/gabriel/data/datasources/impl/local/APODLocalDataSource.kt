package com.gabriel.data.datasources.impl.local

import androidx.lifecycle.LiveData
import com.gabriel.data.datasources.ApodDAO
import com.gabriel.data.datasources.defs.APODDataSourceDef
import com.gabriel.data.models.APOD
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal class APODLocalDataSource(private val apodDao: ApodDAO) : APODDataSourceDef {

    override suspend fun fetchAstronomyPictureOfTheDay(): APOD? {
        return apodDao.getApodByDate(
            LocalDate.now().format(DateTimeFormatter.ofPattern(APOD.DATE_FORMAT))
        )
    }

    override fun fetchAstronomyPicturesLive(): LiveData<List<APOD>> {
        //When fetching from Room DB we fetch everything irrespective of the date range
        return apodDao.getApods()
    }

    override suspend fun fetchAstronomyPictureByDate(date: String): APOD? {
        return apodDao.getApodByDate(date)
    }

    override suspend fun saveApodsToDb(vararg apod: APOD) {
        apodDao.saveApods(*apod)
    }

    override suspend fun deleteApods(vararg apod: APOD) {
        apodDao.deleteApod(*apod)
    }

}