package com.gabriel.astronomypod.features.viewApod

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gabriel.apod.core.BaseViewModel
import com.gabriel.data.models.APOD
import com.gabriel.data.repos.ApodRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context

@HiltViewModel
class ViewApodViewModel @Inject constructor(
    @ApplicationContext app: Context,
    private val apodRepo: ApodRepo
) : BaseViewModel(app) {

    //UI States
    val isLoading = MutableLiveData(true)
    val isError = MutableLiveData(false)

    val title = MutableLiveData<String>()
    val explanation = MutableLiveData<String>()
    val date = MutableLiveData<String>()

    val currentApod = MutableLiveData<APOD>()

    fun fetchApod(date: String) {
        isLoading.value = true
        viewModelScope.launch {
            currentApod.value = apodRepo.fetchAstronomyPictureByDate(date)?.also {
                title.value = it.title
                explanation.value = it.explanation
                this@ViewApodViewModel.date.value =
                    LocalDate.parse(it.date).format(DateTimeFormatter.ofPattern("MMM dd, YYYY"))
            } ?: run {
                isLoading.value = false
                isError.value = true
                return@launch
            }
        }
    }
}