package com.gabriel.data.datasources

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gabriel.data.models.APOD
import com.gabriel.data.models.APOD.Companion.APOD_TABLE

@Database(entities = [APOD::class], version = 2)
abstract class ApodDatabase : RoomDatabase() {
    abstract fun apodDao(): ApodDAO
}

object DBMigrations{
  fun migration1_2() = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
      db.execSQL(
        "CREATE TABLE IF NOT EXISTS ${APOD_TABLE}Tmp (date TEXT PRIMARY KEY NOT NULL, title TEXT NOT NULL, url TEXT NULL, explanation TEXT NOT NULL, mediaType TEXT NOT NULL, hdUrl TEXT NULL)"
      )
      db.execSQL(
        "INSERT INTO ${APOD_TABLE}Tmp (date, title, url, explanation, hdUrl, mediaType) SELECT date, title, url, explanation, hdUrl, mediaType FROM $APOD_TABLE"
      )
      db.execSQL("DROP TABLE $APOD_TABLE")

      db.execSQL(
        "ALTER TABLE ${APOD_TABLE}Tmp RENAME TO $APOD_TABLE"
      )

      db.execSQL(
        "ALTER TABLE $APOD_TABLE ADD COLUMN thumbnail TEXT"
      )
    }
  }
}