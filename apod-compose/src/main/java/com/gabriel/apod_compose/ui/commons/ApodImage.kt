package com.gabriel.apod_compose.ui.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.gabriel.apod_compose.R
import com.gabriel.data.models.APOD
import kotlinx.coroutines.flow.Flow

@Composable
fun ApodImage(
  modifier: Modifier = Modifier,
  pod: APOD?
) {

  val painter = rememberAsyncImagePainter(model = pod?.hdUrl ?: pod?.url)

  Box(modifier = modifier) {
    if (pod == null || painter.state !is AsyncImagePainter.State.Success) {
      CircularProgressIndicator(
        modifier = Modifier.align(Alignment.Center)
      ) //use custom progress indicator
    }

    Image(
      modifier = Modifier.fillMaxSize(),
      contentScale = ContentScale.Crop,
      painter = painter,
      contentDescription = ""
    )
  }

}