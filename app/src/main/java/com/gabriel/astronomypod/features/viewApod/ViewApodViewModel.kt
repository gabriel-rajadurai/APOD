package com.gabriel.astronomypod.features.viewApod

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.gabriel.astronomypod.common.BaseViewModel
import com.gabriel.data.models.APOD
import com.gabriel.data.repos.ApodRepo

class ViewApodViewModel(val app: Application, private val apodDate: String) : BaseViewModel(app) {

    private val apodRepo by lazy { ApodRepo(getApplication()) }
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