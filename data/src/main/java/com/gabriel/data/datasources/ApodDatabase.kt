package com.gabriel.data.datasources

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gabriel.data.models.APOD

@Database(entities = [APOD::class], version = 1)
abstract class ApodDatabase : RoomDatabase() {
    abstract fun apodDao(): ApodDAO

    companion object {

        @Volatile
        private var INSTANCE: ApodDatabase? = null

        fun getDatabase(context: Context): ApodDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    ApodDatabase::class.java,
                    "Apod_Database"
                ).build()
                return INSTANCE!!
            }
        }
    }
}