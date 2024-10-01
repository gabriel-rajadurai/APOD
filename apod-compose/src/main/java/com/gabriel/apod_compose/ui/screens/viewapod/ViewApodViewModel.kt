package com.gabriel.apod_compose.ui.screens.viewapod

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.gabriel.apod_compose.navigation.Screen
import com.gabriel.data.repos.ApodRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class ViewApodViewModel @Inject constructor(
  apodRepo: ApodRepo,
  savedStateHandle: SavedStateHandle
) : ViewModel() {

  val apod = flow {
    val date = requireNotNull(savedStateHandle.toRoute<Screen.ViewApod>().podDate)
    apodRepo.fetchAstronomyPictureByDate(date)?.let {
      emit(Result.success(it))
    } ?: emit(Result.failure(Exception("")))
  }

}