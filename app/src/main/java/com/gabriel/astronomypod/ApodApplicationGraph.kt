package com.gabriel.astronomypod

import android.app.Application
import com.gabriel.astronomypod.features.apodList.ApodListFragment
import com.gabriel.astronomypod.features.apodOfTheDay.APODTodayFragment
import com.gabriel.astronomypod.features.viewApod.ViewApodActivity
import com.gabriel.data.dagger.modules.APODNetworkModule
import com.gabriel.data.dagger.modules.APODRoomModule
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, APODNetworkModule::class, APODRoomModule::class])
interface ApodApplicationGraph {

    fun inject(apodTodayFragment: APODTodayFragment)
    fun inject(apodListFragment: ApodListFragment)
    fun inject(viewApodActivity: ViewApodActivity)

}

@Module
class AppModule(private val app: ApodApplication) {
    @Provides
    fun provideApplication(): Application = app
}