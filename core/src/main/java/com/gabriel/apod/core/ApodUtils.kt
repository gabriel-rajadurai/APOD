package com.gabriel.apod.core

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.core.app.ShareCompat
import com.gabriel.data.models.APOD

object ApodUtils {

  fun share(context: Context, apod: APOD) {
    ShareCompat.IntentBuilder(context)
      .setType("text/plain")
      .setChooserTitle("Share APOD")
      .setSubject(apod.title)
      .setText("${apod.explanation} \n\n View it here -> ${apod.hdUrl ?: apod.url}")
      .startChooser()
  }

  fun download(context: Context, apod: APOD) {
    val ext = MimeTypeMap.getFileExtensionFromUrl(apod.hdUrl ?: apod.url)
    val downloadRequest =
      DownloadManager.Request(Uri.parse(apod.hdUrl ?: apod.url))
        .setDestinationInExternalPublicDir(
          Environment.DIRECTORY_DOWNLOADS,
          "APOD/${apod.title}_${apod.date}.$ext"
        )
        .setDescription("Downloading Image")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(true)
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

    val dm =
      context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    dm.enqueue(downloadRequest)
  }
}