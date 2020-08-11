package com.gabriel.astronomypod.features.apodOfTheDay

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.gabriel.astronomypod.R
import com.gabriel.astronomypod.common.BaseViewModel
import com.gabriel.astronomypod.common.getString
import com.gabriel.data.repos.ApodRepo
import javax.inject.Inject

class APODTodayViewModel @Inject constructor(app: Application) : BaseViewModel(app) {

    @Inject
    lateinit var apodRepo: ApodRepo
    var error = MutableLiveData<String>()

    val apodOfTheDay = liveData {
        try {
            emit(apodRepo.fetchAstronomyPictureOfTheDay())
        } catch (e: Exception) {
            e.printStackTrace()
            error.value = getString(R.string.error_unable_to_fetch)
        }
    }
}