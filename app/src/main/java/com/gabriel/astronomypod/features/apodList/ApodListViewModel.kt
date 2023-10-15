package com.gabriel.astronomypod.features.apodList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gabriel.astronomypod.common.BaseViewModel
import com.gabriel.data.models.APOD
import com.gabriel.data.repos.ApodRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import androidx.lifecycle.switchMap

@HiltViewModel
class ApodListViewModel @Inject constructor(
    @ApplicationContext app: Context,
    private val apodRepo: ApodRepo
) : BaseViewModel(app) {

    //UI States
    val isLoading = MutableLiveData(true)
    val isError = MutableLiveData(false)

    private val _getAstronomyPictures = MutableLiveData<Boolean>()
    val apodList = _getAstronomyPictures.switchMap {
        fetchApodList()
    }

    fun getAstronomyPictures() {
        isLoading.value = true
        _getAstronomyPictures.value = true
    }

    suspend fun fetchApod(date: String) = apodRepo.fetchAstronomyPictureByDate(date)

    private fun fetchApodList(): LiveData<List<APOD>> {
        fetchApodsFromServer()
        return apodRepo.fetchAstronomyPicturesLive()
    }

    private fun fetchApodsFromServer() {
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

}