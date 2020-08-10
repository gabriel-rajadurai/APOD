package com.gabriel.data.dagger.modules

import android.app.Application
import androidx.room.Room
import com.gabriel.data.datasources.ApodDAO
import com.gabriel.data.datasources.ApodDatabase
import com.gabriel.data.datasources.impl.local.APODLocalDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class APODRoomModule(app: Application) {

    private val database by lazy {
        Room.databaseBuilder(
            app,
            ApodDatabase::class.java,
            "Apod_Database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideApodDatabase() = database

    @Singleton
    @Provides
    fun provideApodDao() = database.apodDao()

    @Singleton
    @Provides
    fun provideApodLocalDataSource(dao: ApodDAO) = APODLocalDataSource(dao)
}