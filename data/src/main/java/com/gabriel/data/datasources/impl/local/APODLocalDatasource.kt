package com.gabriel.data.datasources.impl.local

import com.gabriel.data.datasources.ApodDAO
import com.gabriel.data.datasources.defs.APODDataSourceDef
import com.gabriel.data.models.APOD
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class APODLocalDatasource(private val apodDao: ApodDAO) : APODDataSourceDef {


    override suspend fun fetchAstronomyPictureOfTheDay(): APOD? {
        return apodDao.getApodByDate(
            LocalDate.now().format(DateTimeFormatter.ofPattern(APOD.DATE_FORMAT))
        )
    }

    override suspend fun fetchAstronomyPictures(fromDate: String, endDate: String): List<APOD>? {
        return apodDao.getApods(fromDate, endDate)
    }

    override suspend fun fetchAstronomyPictureByDate(date: String): APOD? {
        return apodDao.getApodByDate(date)
    }

    override suspend fun saveApodToDb(apod: APOD) {
        apodDao.saveApod(apod)
    }

    override suspend fun deleteApods(vararg apod: APOD) {
        apodDao.deleteApod(*apod)
    }

}