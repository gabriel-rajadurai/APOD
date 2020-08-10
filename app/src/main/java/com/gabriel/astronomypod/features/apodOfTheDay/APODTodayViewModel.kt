package com.gabriel.astronomypod.features.apodOfTheDay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.gabriel.data.repos.ApodRepo

class APODTodayViewModel : ViewModel() {

    private val apodRepo by lazy { ApodRepo() }

    val apodOfTheDay = liveData {
        emit(apodRepo.fetchAstronomyPictureOfTheDay())
    }
}