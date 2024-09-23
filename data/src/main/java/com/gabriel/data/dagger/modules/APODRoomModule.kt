package com.gabriel.data.dagger.modules

<<<<<<< Updated upstream
import android.app.Application
=======
import android.content.Context
>>>>>>> Stashed changes
import androidx.room.Room
import com.gabriel.data.datasources.ApodDAO
import com.gabriel.data.datasources.ApodDatabase
import com.gabriel.data.datasources.defs.APODDataSourceDef
import com.gabriel.data.datasources.impl.local.APODLocalDataSource
import dagger.Module
import dagger.Provides
<<<<<<< Updated upstream
=======
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
>>>>>>> Stashed changes
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

    @LocalSource
    @Singleton
    @Provides
<<<<<<< Updated upstream
    fun provideApodDao() = database.apodDao()

    @Singleton
    @Provides
    fun provideApodLocalDataSource(dao: ApodDAO) = APODLocalDataSource(dao)
}
=======
    fun provideApodLocalDataSource(dao: ApodDAO): APODDataSourceDef {
        return APODLocalDataSource(dao)
    }

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalSource
>>>>>>> Stashed changes
