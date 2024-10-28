package com.gabriel.apod_compose.ui.screens.today

import androidx.lifecycle.ViewModel
import com.gabriel.data.repos.ApodRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class TodayApodViewModel @Inject constructor(
  apodRepo: ApodRepo
) : ViewModel() {

  val apod = flow {
    try {
      val pod = apodRepo.fetchAstronomyPictureOfTheDay()
      if (pod == null){
        emit(Result.failure(Exception("")))
      } else{
        emit(Result.success(pod))
      }
    } catch (e : Exception){
      e.printStackTrace()
      emit(Result.failure(e))
    }
  }

}