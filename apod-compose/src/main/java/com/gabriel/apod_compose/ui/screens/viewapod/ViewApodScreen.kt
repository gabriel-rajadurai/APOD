package com.gabriel.apod_compose.ui.screens.viewapod

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gabriel.apod.core.ApodUtils
import com.gabriel.apod_compose.R
import com.gabriel.apod_compose.commons.LoadingIndicator
import com.gabriel.apod_compose.commons.isTablet
import com.gabriel.apod_compose.ui.commons.ApodImage
import com.gabriel.apod_compose.ui.screens.apodlist.Action
import com.gabriel.data.models.APOD

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ViewApodScreen(
  modifier: Modifier = Modifier,
  navigateBack: () -> Unit
) {

  val viewModel: ViewApodViewModel = hiltViewModel()
  val context = LocalContext.current

  val apodResult = viewModel.apod.collectAsStateWithLifecycle(initialValue = null).value

  Box(modifier = modifier) {

    if (apodResult == null) {
      LoadingIndicator(
        modifier = Modifier.align(Alignment.Center)
      )
      return
    }

    if (apodResult.isSuccess) {
      val apod = apodResult.getOrNull()
      if (apod == null) {
        Box(modifier = Modifier.fillMaxSize())
        return
      }

      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
      ) {

        stickyHeader("toolbar") {
          TopAppBar(
            title = {
              Text(
                text = apod.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            },
            colors = TopAppBarDefaults.topAppBarColors().copy(
              containerColor = Color.Transparent
            ),
            modifier = Modifier
              .fillMaxWidth(),
            navigationIcon = {
              IconButton(onClick = navigateBack) {
                Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = "")
              }
            },
            actions = {
              if (apod.mediaType == APOD.MEDIA_TYPE_IMAGE) {
                IconButton(onClick = {
                  onAction(context, apod, Action.Share)
                }) {
                  Icon(painter = painterResource(id = R.drawable.ic_share), contentDescription = "")
                }

                IconButton(onClick = {
                  onAction(context, apod, Action.Download)
                }) {
                  Icon(
                    painter = painterResource(id = R.drawable.ic_download),
                    contentDescription = ""
                  )
                }
              }
            }
          )
        }

        item("image") {
          ApodImage(
            modifier = Modifier
              .fillMaxWidth()
              .height(
                if (isTablet()) {
                  800.dp
                } else {
                  600.dp
                }
              ),
            pod = apod,
            contentScale = ContentScale.Fit
          )
        }

        item("divider") {
          HorizontalDivider()
        }

        item("desc") {
          Text(
            text = apod.explanation,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(10.dp)
          )
        }

        item("space") {
          Spacer(modifier = Modifier.size(16.dp))
        }

        item("date") {
          Text(
            text = apod.date,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
              .fillMaxSize()
              .padding(10.dp),
            textAlign = TextAlign.End
          )
        }
      }
    } else {

    }
  }
}

fun onAction(
  context: Context,
  apod: APOD,
  action: Action
) {
  when (action) {
    Action.Download -> {
      ApodUtils.download(context, apod)
    }

    Action.Share -> {
      ApodUtils.share(context, apod)
    }

    Action.View -> {
      //do nothing, this action is not possible in this creen
    }
  }
}

const val EXTRAS_APOD_DATE = "EXTRAS_APOD_DATE"