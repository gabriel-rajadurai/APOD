package com.gabriel.apod_compose.ui.screens.apodlist

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gabriel.apod_compose.R
import com.gabriel.apod_compose.ui.commons.ApodImage
import com.gabriel.apod_compose.ui.theme.AstronomyPODTheme
import com.gabriel.data.models.APOD

@Composable
fun ApodListItem(
  modifier: Modifier = Modifier,
  pod: APOD? = null,
  onAction: (Action) -> Unit
) {

  var isExpanded by remember {
    mutableStateOf(false)
  }

  val infoViewAlpha: Float by animateFloatAsState(
    targetValue = if (isExpanded) 1f else 0f,
    animationSpec = tween(
      durationMillis = 200,
      easing = LinearEasing,
    ),
    label = "expandCollapse"
  )

  val expandIconRotation: Float by animateFloatAsState(
    targetValue = if (isExpanded) 180f else 0f,
    animationSpec = tween(
      durationMillis = 200,
      easing = LinearEasing,
    ),
    label = "expandCollapse"
  )



  Box(modifier = modifier) {

    ApodImage(
      modifier = Modifier
        .fillMaxSize()
        .clickable {
          onAction(Action.View)
        },
      pod = pod
    )

    Column(
      modifier = Modifier
        .fillMaxSize()
    ) {

      InfoView(infoViewAlpha, pod, onAction)

      Row(
        modifier = Modifier
          .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
          .clickable {
            isExpanded = !isExpanded
          }
          .padding(10.dp)
      ) {
        Text(
          text = pod?.title ?: "",
          maxLines = 1,
          modifier = Modifier.weight(1f, true),
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.bodyMedium
        )
        Icon(
          modifier = Modifier.rotate(expandIconRotation),
          painter = painterResource(id = R.drawable.ic_expand),
          contentDescription = ""
        )
      }
    }
  }
}

@Composable
private fun ColumnScope.InfoView(
  infoViewAlpha: Float,
  pod: APOD?,
  onAction: (Action) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .weight(1f, true)
      .alpha(infoViewAlpha),
  ) {

    Text(
      text = pod?.date ?: "",
      modifier = Modifier.padding(10.dp),
      style = MaterialTheme.typography.bodyMedium
    )

    Text(
      text = pod?.explanation ?: "",
      maxLines = 4,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier.padding(10.dp),
      style = MaterialTheme.typography.bodyMedium
    )

    Spacer(modifier = Modifier.weight(1f, true))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.End
    ) {
      IconButton(onClick = {
        onAction(Action.View)
      }) {
        Icon(
          painter = painterResource(id = R.drawable.ic_open),
          contentDescription = ""
        )
      }

      if (pod?.mediaType == APOD.MEDIA_TYPE_IMAGE){
        IconButton(onClick = {
          onAction(Action.Share)
        }) {
          Icon(painter = painterResource(id = R.drawable.ic_share), contentDescription = "")
        }

        IconButton(onClick = {
          onAction(Action.Download)
        }) {
          Icon(
            painter = painterResource(id = R.drawable.ic_download),
            contentDescription = ""
          )
        }
      }


    }
  }
}

@Composable
@Preview
private fun ApodListItemPreview() {
  AstronomyPODTheme {
    Surface {
      ApodListItem(
        modifier = Modifier
          .fillMaxWidth()
          .height(300.dp),
        APOD(
          "Sept 30, 2024",
          "Comet Tsuchinshan-ATLAS over Mexico",
          "",
          "The new comet has passed its closest to the Sun and is now moving closer to the Earth. C/2023 A3 (Tsuchinshan-ATLAS) is currently moving out from inside the orbit of Venus and on track to pass its nearest to the Earth",
          "",
          "",
          ""
        ),
        onAction = {}
      )
    }
  }
}