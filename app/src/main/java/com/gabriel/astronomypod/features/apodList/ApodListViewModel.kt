package com.gabriel.astronomypod.features.apodList

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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ApodListViewModel @Inject constructor(app: Application) : BaseViewModel(app) {

    @Inject
    lateinit var apodRepo: ApodRepo
    val apodList = MutableLiveData<List<APOD>?>()
    var error = MutableLiveData<String>()

    fun fetchApodList() {
        viewModelScope.launch {
            try {
                apodList.value = apodRepo.fetchAstronomyPictures(
                    LocalDate.now().minusDays(30)
                        .format(DateTimeFormatter.ofPattern(APOD.DATE_FORMAT)),
                    LocalDate.now().format(DateTimeFormatter.ofPattern(APOD.DATE_FORMAT))
                )?.sortedByDescending { it.date }
            } catch (e: Exception) {
                error.value = getString(R.string.error_unable_to_fetch)
            }
        }
    }

    suspend fun fetchApod(date: String) = suspendCoroutine<APOD?> {
        viewModelScope.launch {
            it.resume(apodRepo.fetchAstronomyPictureByDate(date))
        }
    }
}