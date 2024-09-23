package com.gabriel.apod_compose

import android.app.Application
import com.gabriel.data.dagger.modules.APODNetworkModule
import com.gabriel.data.dagger.modules.APODRoomModule
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApodApplication : Application() {

}