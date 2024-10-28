package com.gabriel.astronomypod.features.apodOfTheDay

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gabriel.astronomypod.R
import com.gabriel.apod.core.BaseViewModel
import com.gabriel.data.models.APOD
import com.gabriel.data.repos.ApodRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class APODTodayViewModel @Inject constructor(
    @ApplicationContext app: Context,
    private val apodRepo: ApodRepo
) : BaseViewModel(app) {

    //UI States
    val isLoading = MutableLiveData(true)
    var error = MutableLiveData<String?>()

    val apodOfTheDay = MutableLiveData<APOD>()

    fun fetchApodOfTheDay() {
        viewModelScope.launch {
            isLoading.value = true
            error.value = null
            try {
                apodOfTheDay.value = apodRepo.fetchAstronomyPictureOfTheDay() ?: run {
                    isLoading.value = false
                    error.value = null
                    return@launch
                }
            } catch (e: Exception) {
                isLoading.value = false
                error.value = getString(R.string.error_unable_to_fetch)
                e.printStackTrace()
            }
        }
    }
}