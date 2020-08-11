package com.gabriel.data.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gabriel.data.api.apodService
import com.gabriel.data.datasources.ApodDAO
import com.gabriel.data.datasources.ApodDatabase
import com.gabriel.data.models.APOD
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RunWith(AndroidJUnit4::class)
// Making ApodsDbTest a LifeCycleOwner by implementing LifeCycleOwner. We do this to test LiveData objects
class ApodsDbTest : LifecycleOwner {
    private lateinit var apodsDao: ApodDAO
    private lateinit var db: ApodDatabase

    // Creating a LifeCycle object with Lifecycle provider as this
    private val lifecycle = LifecycleRegistry(this)

    init {
        // Invoke LifeCycle Event ON_RESUME to start the LifeCycle.
        // Without this LiveData observers won't trigger
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    // This rule swaps the background asynchronous task executor with a synchronous one
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            ctx,
            ApodDatabase::class.java
        ).build()
        apodsDao = db.apodDao()

        val response = apodService.fetchAstronomyPictures(
            LocalDate.now().minusDays(30)
                .format(DateTimeFormatter.ofPattern(APOD.DATE_FORMAT)),
            LocalDate.now().format(
                DateTimeFormatter.ofPattern(APOD.DATE_FORMAT)
            )
        ).execute()
        runBlocking {
            response.body()?.let {
                apodsDao.saveApods(*it.toTypedArray())
            }
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun fetchApodOfTheDay() {
        runBlocking {
            assertNotNull(
                apodsDao.getApodByDate(
                    LocalDate.now().format(DateTimeFormatter.ofPattern(APOD.DATE_FORMAT))
                )
            )
        }
    }

    @Test
    fun fetchAllApods() {
        apodsDao.getApods().observe(this, Observer {
            assertThat(it.isNotEmpty(), CoreMatchers.equalTo(true))
            // Invoke Lifecycle Event ON_DESTROY to prevent the Observer from running for ever
            lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        })
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }
}