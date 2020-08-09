package com.gabriel.astronomypod.features.apodList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.gabriel.data.models.APOD
import com.gabriel.data.repos.ApodRepo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ApodListViewModel : ViewModel() {

    private val apodRepo by lazy { ApodRepo() }
    val apodlist = liveData<List<APOD>?> {
        emit(apodRepo.fetchAstronomyPictures(
            LocalDate.now().minusDays(30).format(DateTimeFormatter.ofPattern("YYYY-MM-dd")),
            LocalDate.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"))
        ))
    }
}