package com.gabriel.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "apod_table")
data class APOD(
  @PrimaryKey
  val date: String,
  val title: String,
  val url: String? = null,
  val explanation: String,
  @SerializedName(HD_URL)
  val hdUrl: String? = null,
  @SerializedName(THUMBNAIL)
  val thumbnail: String? = null,
  @SerializedName(MEDIA_TYPE)
  val mediaType: String
) {

  companion object {
    private const val HD_URL = "hdurl"
    private const val MEDIA_TYPE = "media_type"
    private const val THUMBNAIL = "thumbnail_url"
    const val MEDIA_TYPE_IMAGE = "image"
    const val MEDIA_TYPE_VIDEO = "video"
    const val MEDIA_TYPE_OTHER = "other"
    const val DATE_FORMAT = "yyyy-MM-dd"

    const val APOD_TABLE = "apod_table"
  }
}