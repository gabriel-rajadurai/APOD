package com.gabriel.astronomypod

import android.app.Application
import com.gabriel.data.dagger.modules.APODNetworkModule
import com.gabriel.data.dagger.modules.APODRoomModule

class ApodApplication : Application() {

    lateinit var appGraph: ApodApplicationGraph

    override fun onCreate() {
        super.onCreate()
        appGraph = DaggerApodApplicationGraph.builder()
            .appModule(AppModule(this))
            .aPODRoomModule(APODRoomModule(this))
            .aPODNetworkModule(APODNetworkModule())
            .build()
    }
}