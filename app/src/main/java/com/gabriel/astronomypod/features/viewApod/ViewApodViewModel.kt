package com.gabriel.astronomypod.features.viewApod

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.gabriel.data.models.APOD
import com.gabriel.data.repos.ApodRepo

class ViewApodViewModel(private val apodDate: String) : ViewModel() {

    private val apodRepo by lazy { ApodRepo() }
    val title = MutableLiveData<String>()
    val explanation = MutableLiveData<String>()
    val date = MutableLiveData<String>()

    val currentApod = liveData<APOD?> {
        apodRepo.fetchAstronomyPictureByDate(apodDate)?.let {
            title.value = it.title
            explanation.value = it.explanation
            date.value = it.date
            emit(it)
        } ?: emit(null)
    }
}