package com.gabriel.data.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gabriel.data.models.APOD
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RunWith(AndroidJUnit4::class)
class FetchApodsTest {

    @Test
    fun fetchApodOfTheDay() {
        try {
            val response = apodService.fetchAstronomyPictureOfTheDay().execute()
            assert(response.isSuccessful && response.body() != null)
        } catch (e: IOException) {
            assert(false)
        }
    }

    @Test
    fun fetchApodOfLast30Days() {
        try {
            val response = apodService.fetchAstronomyPictures(
                LocalDate.now().minusDays(30)
                    .format(DateTimeFormatter.ofPattern(APOD.DATE_FORMAT)),
                LocalDate.now().format(DateTimeFormatter.ofPattern(APOD.DATE_FORMAT))
            ).execute()
            assert(response.isSuccessful && !response.body().isNullOrEmpty())
        } catch (e: IOException) {
            assert(false)
        }
    }

    @Test
    fun fetchApodByDate() {
        try {
            val response = apodService.fetchAstronomyPictureByDate(
                "2020-06-01"
            ).execute()
            assert(response.isSuccessful && response.body() != null)
        } catch (e: IOException) {
            assert(false)
        }
    }
}