package com.gabriel.data.repos

import androidx.lifecycle.LiveData
import com.gabriel.data.dagger.modules.LocalSource
import com.gabriel.data.dagger.modules.RemoteSource
import com.gabriel.data.datasources.defs.APODDataSourceDef
import com.gabriel.data.datasources.impl.local.APODLocalDataSource
import com.gabriel.data.datasources.impl.remote.APODRemoteDataSource
import com.gabriel.data.models.APOD
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApodRepo @Inject constructor(
    @RemoteSource private val apodRemoteDs : APODDataSourceDef,
    @LocalSource private val apodLocalDs : APODDataSourceDef,
) : APODDataSourceDef {

    override suspend fun fetchAstronomyPictureOfTheDay(): APOD? {
        //First we check if today's picture is already in db
        //If not, we fetch from server
        //Unfortunately there is an issue in the Nasa APOD api which makes the server call return with an error
        //In such scenarios, we fetch data of the previous day

        return apodLocalDs.fetchAstronomyPictureOfTheDay()
            ?: apodRemoteDs.fetchAstronomyPictureOfTheDay()?.also {
                apodLocalDs.saveApodsToDb(it)
            } ?: fetchAstronomyPictureByDate(
                LocalDate.now().minusDays(1).format(
                    DateTimeFormatter.ofPattern(APOD.DATE_FORMAT)
                )
            )
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