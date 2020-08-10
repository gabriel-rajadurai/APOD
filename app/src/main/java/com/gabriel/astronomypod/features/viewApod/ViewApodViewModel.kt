package com.gabriel.astronomypod.features.viewApod

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gabriel.astronomypod.common.BaseViewModel
import com.gabriel.data.models.APOD
import com.gabriel.data.repos.ApodRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewApodViewModel @Inject constructor(val app: Application) :
    BaseViewModel(app) {

    @Inject
    lateinit var apodRepo: ApodRepo
    val title = MutableLiveData<String>()
    val explanation = MutableLiveData<String>()
    val date = MutableLiveData<String>()

    val currentApod = MutableLiveData<APOD>()

    fun fetchApod(date: String) {
        viewModelScope.launch {
            currentApod.value = apodRepo.fetchAstronomyPictureByDate(date)?.also {
                title.value = it.title
                explanation.value = it.explanation
                this@ViewApodViewModel.date.value = it.date
            }
        }
    }
}