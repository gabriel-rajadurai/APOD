package com.gabriel.astronomypod.features.apodOfTheDay

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.gabriel.astronomypod.R
import com.gabriel.astronomypod.common.BaseViewModel
import com.gabriel.astronomypod.common.getString
import com.gabriel.data.models.APOD
import com.gabriel.data.repos.ApodRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

class APODTodayViewModel @Inject constructor(app: Application) : BaseViewModel(app) {

    @Inject
    lateinit var apodRepo: ApodRepo
    var error = MutableLiveData<String>()

    val apodOfTheDay = MutableLiveData<APOD>()

    fun fetchApodOfTheDay() {
        viewModelScope.launch {
            try {
                apodOfTheDay.value = apodRepo.fetchAstronomyPictureOfTheDay()
            } catch (e: Exception) {
                e.printStackTrace()
                error.value = getString(R.string.error_unable_to_fetch)
            }
        }
    }
}