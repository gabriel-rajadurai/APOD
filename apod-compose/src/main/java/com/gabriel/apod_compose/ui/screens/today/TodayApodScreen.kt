package com.gabriel.apod_compose.ui.screens.today

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gabriel.apod_compose.ui.commons.ApodImage
import com.gabriel.apod_compose.ui.theme.AstronomyPODTheme

@Composable
fun TodayApodScreen(
  modifier: Modifier = Modifier
) {

  val viewModel: TodayApodViewModel = hiltViewModel()
  val podResult = viewModel.apod.collectAsStateWithLifecycle(initialValue = null).value

  Box(
    modifier = modifier
  ) {

    if (podResult == null) {
      CircularProgressIndicator(
        modifier = Modifier.align(Alignment.Center)
      )
    } else if (podResult.isSuccess) {
      val pod = podResult.getOrNull()
      ApodImage(modifier = Modifier.fillMaxSize(), pod = pod)

      Column(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .fillMaxWidth()
          .padding(24.dp)
      ) {

        Text(text = pod?.title ?: "", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.size(10.dp))

        Text(
          text = pod?.explanation ?: "",
          style = MaterialTheme.typography.bodyMedium,
          overflow = TextOverflow.Ellipsis,
          maxLines = 2
        )

        Spacer(modifier = Modifier.size(40.dp))

        OutlinedButton(
          onClick = { /*TODO*/ },
          modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text(
            text = "Discover more".uppercase(),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
          )
        }

      }

    } else {
      //failure
    }

    Text(
      text = "Today's Picture",
      modifier = Modifier
        .align(Alignment.TopCenter)
        .padding(24.dp),
      style = MaterialTheme.typography.titleLarge
    )
  }
}


@Preview(
  uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun TodayApodScreenPreview() {
  AstronomyPODTheme {
    Surface {
      TodayApodScreen(
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}