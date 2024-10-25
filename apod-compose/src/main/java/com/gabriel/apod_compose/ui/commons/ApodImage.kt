package com.gabriel.apod_compose.ui.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.vectorResource
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.gabriel.apod_compose.R
import com.gabriel.apod_compose.commons.LoadingIndicator
import com.gabriel.data.models.APOD

@Composable
fun ApodImage(
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Crop,
  pod: APOD?
) {

  if (LocalInspectionMode.current) {
    Box(modifier = modifier) {
      LoadingIndicator(
        modifier = Modifier.align(Alignment.Center)
      )
    }
    return
  }

  when (pod?.mediaType) {
    APOD.MEDIA_TYPE_VIDEO -> {
      val painter = rememberAsyncImagePainter(model = pod.thumbnail)
      Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
      ) {
        if (painter.state !is AsyncImagePainter.State.Success) {
          LoadingIndicator(
            modifier = Modifier.align(Alignment.Center)
          )
        }
        Image(
          modifier = Modifier.fillMaxSize(),
          contentScale = contentScale,
          painter = painter,
          contentDescription = ""
        )
        Image(
          imageVector = ImageVector.vectorResource(id = R.drawable.ic_play),
          contentDescription = ""
        )
      }
    }
    APOD.MEDIA_TYPE_IMAGE -> {
      val painter = rememberAsyncImagePainter(model = pod.hdUrl ?: pod.url)
      Box(modifier = modifier) {
        if (painter.state !is AsyncImagePainter.State.Success) {
          LoadingIndicator(
            modifier = Modifier.align(Alignment.Center)
          )
        }

        Image(
          modifier = Modifier.fillMaxSize(),
          contentScale = contentScale,
          painter = painter,
          contentDescription = ""
        )
      }
    }
    else -> {
      Box(
        modifier = modifier,
      )
    }
  }


}