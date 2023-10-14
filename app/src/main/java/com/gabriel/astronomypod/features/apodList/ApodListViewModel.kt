package com.gabriel.astronomypod.features.apodList

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.gabriel.astronomypod.R
import com.gabriel.astronomypod.common.BaseViewModel
import com.gabriel.data.models.APOD
import com.gabriel.data.repos.ApodRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context

@HiltViewModel
class ApodListViewModel @Inject constructor(
    @ApplicationContext app : Context,
    private val apodRepo: ApodRepo
) : BaseViewModel(app) {
    fun fetchApodsFromServer() {
        viewModelScope.launch {
            try {
                apodRepo.fetchAstronomyPictures(
                    LocalDate.now().minusDays(30)
                        .format(DateTimeFormatter.ofPattern(APOD.DATE_FORMAT)),
                    LocalDate.now().format(DateTimeFormatter.ofPattern(APOD.DATE_FORMAT))
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchApodList(): LiveData<List<APOD>> {
        fetchApodsFromServer()
        return apodRepo.fetchAstronomyPicturesLive()
    }

    suspend fun fetchApod(date: String) = apodRepo.fetchAstronomyPictureByDate(date)


}