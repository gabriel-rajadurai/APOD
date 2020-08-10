package com.gabriel.astronomypod.features.apodList

import android.app.Application
import androidx.lifecycle.liveData
import com.gabriel.astronomypod.common.BaseViewModel
import com.gabriel.data.models.APOD
import com.gabriel.data.repos.ApodRepo
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ApodListViewModel @Inject constructor(app: Application) : BaseViewModel(app) {

    @Inject
    lateinit var apodRepo: ApodRepo
    val apodList = liveData {
        emit(
            apodRepo.fetchAstronomyPictures(
                LocalDate.now().minusDays(30).format(DateTimeFormatter.ofPattern(APOD.DATE_FORMAT)),
                LocalDate.now().format(DateTimeFormatter.ofPattern(APOD.DATE_FORMAT))
            )?.sortedByDescending { it.date }
        )
    }
}