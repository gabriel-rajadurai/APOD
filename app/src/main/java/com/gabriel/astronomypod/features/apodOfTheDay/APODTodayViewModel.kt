package com.gabriel.astronomypod.features.apodOfTheDay

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.gabriel.astronomypod.R
import com.gabriel.astronomypod.common.BaseViewModel
import com.gabriel.data.models.APOD
import com.gabriel.data.repos.ApodRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context

@HiltViewModel
class APODTodayViewModel @Inject constructor(
    @ApplicationContext app : Context,
    private val apodRepo: ApodRepo
) : BaseViewModel(app) {

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