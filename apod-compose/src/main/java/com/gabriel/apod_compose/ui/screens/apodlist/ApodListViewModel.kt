package com.gabriel.apod_compose.ui.screens.apodlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.gabriel.data.models.APOD
import com.gabriel.data.repos.ApodRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ApodListViewModel @Inject constructor(
  private val apodRepo: ApodRepo
) : ViewModel() {

  val apodList = apodRepo.fetchAstronomyPicturesLive().asFlow().map {
    if (it.isEmpty()) {
      try {
        val astronomyPictures = fetchApodsFromServer()
        if (astronomyPictures.isNullOrEmpty()) {
          Result.failure(Exception(""))
        } else {
          Result.success(emptyList())
        }
      } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
      }
    } else {
      Result.success(it)
    }
  }

  init {
    viewModelScope.launch { fetchApodsFromServer() }
  }

  private suspend fun fetchApodsFromServer() = apodRepo.fetchAstronomyPictures(
    LocalDate.now().minusDays(30)
      .format(DateTimeFormatter.ofPattern(APOD.DATE_FORMAT)),
    LocalDate.now().format(DateTimeFormatter.ofPattern(APOD.DATE_FORMAT))
  )

}