package com.gabriel.data.models

import com.google.gson.annotations.SerializedName

data class APOD(
    val date: String,
    val title: String,
    val url: String,
    val explanation: String,
    @SerializedName(HD_URL)
    val hdUrl: String,
    @SerializedName(MEDIA_TYPE)
    val mediaType: String
) {

    companion object {
        private const val HD_URL = "hdurl"
        private const val MEDIA_TYPE = "media_type"
        const val MEDIA_TYPE_IMAGE = "image"
        const val MEDIA_TYPE_VIDEO = "video"
    }
}