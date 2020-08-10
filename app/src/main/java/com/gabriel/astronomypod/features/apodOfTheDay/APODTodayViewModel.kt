package com.gabriel.astronomypod.features.apodOfTheDay

import android.app.Application
import androidx.lifecycle.liveData
import com.gabriel.astronomypod.common.BaseViewModel
import com.gabriel.data.repos.ApodRepo

class APODTodayViewModel(app: Application) : BaseViewModel(app) {

    private val apodRepo by lazy { ApodRepo(getApplication()) }

    val apodOfTheDay = liveData {
        emit(apodRepo.fetchAstronomyPictureOfTheDay())
    }
}