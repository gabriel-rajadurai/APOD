package com.gabriel.astronomypod.features.apodList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.gabriel.astronomypod.common.BaseViewModel
import com.gabriel.data.models.APOD
import com.gabriel.data.repos.ApodRepo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ApodListViewModel(app: Application) : BaseViewModel(app) {

    private val apodRepo by lazy { ApodRepo(getApplication()) }
    val apodList = liveData {
        emit(
            apodRepo.fetchAstronomyPictures(
                LocalDate.now().minusDays(30).format(DateTimeFormatter.ofPattern(APOD.DATE_FORMAT)),
                LocalDate.now().format(DateTimeFormatter.ofPattern(APOD.DATE_FORMAT))
            )
        )
    }
}