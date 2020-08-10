package com.gabriel.astronomypod.features.apodOfTheDay

import android.app.Application
import androidx.lifecycle.liveData
import com.gabriel.astronomypod.common.BaseViewModel
import com.gabriel.data.repos.ApodRepo
import javax.inject.Inject

class APODTodayViewModel @Inject constructor(app: Application) : BaseViewModel(app) {

    @Inject
    lateinit var apodRepo: ApodRepo

    val apodOfTheDay = liveData {
        emit(apodRepo.fetchAstronomyPictureOfTheDay())
    }
}