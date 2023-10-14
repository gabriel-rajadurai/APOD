package com.gabriel.data.dagger.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.gabriel.data.datasources.ApodDAO
import com.gabriel.data.datasources.ApodDatabase
import com.gabriel.data.datasources.impl.local.APODLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class APODRoomModule {

    @Singleton
    @Provides
    fun provideApodDatabase(
        @ApplicationContext context: Context
    ): ApodDatabase {
        return Room.databaseBuilder(
            context,
            ApodDatabase::class.java,
            "Apod_Database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideApodDao(
        database: ApodDatabase
    ): ApodDAO {
        return database.apodDao()
    }

    @Singleton
    @Provides
    fun provideApodLocalDataSource(dao: ApodDAO): APODLocalDataSource {
        return APODLocalDataSource(dao)
    }
}