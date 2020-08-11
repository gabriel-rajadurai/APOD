package com.gabriel.data.datasources

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gabriel.data.models.APOD

@Dao
interface ApodDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveApods(vararg apod: APOD)

    @Delete()
    suspend fun deleteApod(vararg apod: APOD)

    @Query("Select * from apod_table where date=:date ")
    suspend fun getApodByDate(date: String): APOD?

    @Query("Select * from apod_table order by date desc")
    fun getApods(): LiveData<List<APOD>>
}