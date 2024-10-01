package com.gabriel.apod_compose.ui.screens.apodlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gabriel.apod_compose.R
import com.gabriel.apod_compose.commons.isTablet
import com.gabriel.apod_compose.ui.theme.AstronomyPODTheme

@Composable
fun ApodListScreen(
  modifier: Modifier = Modifier
) {

  val viewmodel: ApodListViewModel = hiltViewModel()
  val listState = rememberLazyListState()

  val astronomyPictures = viewmodel.apodList.collectAsStateWithLifecycle(initialValue = null).value

  Scaffold(
    modifier = modifier,
    floatingActionButton = {
      ExtendedFloatingActionButton(
        containerColor = MaterialTheme.colorScheme.background,
        text = { Text(text = stringResource(id = R.string.date)) },
        icon = {
          Icon(
            painter = painterResource(id = R.drawable.ic_date),
            contentDescription = ""
          )
        },
        onClick = {

        },
        expanded = listState.isScrollInProgress
      )
    }
  ) {
    if (astronomyPictures == null) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
      }
    } else if (astronomyPictures.isSuccess) {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(it)
      ) {
        items(astronomyPictures.getOrNull()!!) {
          ApodListItem(
            modifier = Modifier
              .fillMaxWidth()
              .height(
                if (isTablet()) {
                  500.dp
                } else {
                  300.dp
                }
              ),
            pod = it,
            onAction = {}
          )
        }
      }
    } else {
      //todo error view
    }
  }

}

@Composable
@Preview
private fun ApodListScreenPreview() {
  AstronomyPODTheme {
    Surface {
      ApodListScreen(modifier = Modifier.fillMaxSize())
    }
  }
}