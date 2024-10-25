package com.gabriel.apod_compose.ui.screens.apodlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gabriel.apod_compose.R
import com.gabriel.apod_compose.commons.LoadingIndicator
import com.gabriel.apod_compose.commons.isTablet
import com.gabriel.apod_compose.ui.commons.DatePickerSelectable
import com.gabriel.apod_compose.ui.theme.AstronomyPODTheme
import com.gabriel.data.models.APOD
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApodListScreen(
  modifier: Modifier = Modifier,
  viewApod: (String) -> Unit
) {

  val viewmodel: ApodListViewModel = hiltViewModel()
  val listState = rememberLazyListState()
  var showDatePicker by remember { mutableStateOf(false) }

  val astronomyPictures = viewmodel.apodList.collectAsStateWithLifecycle(initialValue = null).value


  if (showDatePicker) {
    val datePickerState = rememberDatePickerState(
      selectableDates = DatePickerSelectable
    )
    val confirmEnabled by remember { derivedStateOf { datePickerState.selectedDateMillis != null } }

    DatePickerDialog(
      onDismissRequest = {
        showDatePicker = false
      },
      confirmButton = {
        TextButton(
          onClick = {
            val selectedDate = SimpleDateFormat(
              APOD.DATE_FORMAT,
              Locale.ENGLISH
            ).format(Date(datePickerState.selectedDateMillis!!))
            viewApod(selectedDate)
            showDatePicker = false
          },
          enabled = confirmEnabled
        ) {
          Text(stringResource(R.string.ok))
        }
      },
      dismissButton = {
        TextButton(
          onClick = {
            showDatePicker = false
          }
        ) {
          Text(stringResource(R.string.cancel))
        }
      }
    ) {
      DatePicker(
        state = datePickerState,
      )
    }
  }

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
          showDatePicker = true
        },
        expanded = !listState.isScrollInProgress
      )
    }
  ) {
    if (astronomyPictures == null) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LoadingIndicator()
      }
    } else if (astronomyPictures.isSuccess) {
      LazyColumn(
        state = listState,
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
            onAction = { action ->
              when (action) {
                Action.Download -> TODO()
                Action.Share -> TODO()
                Action.View -> {
                  viewApod(it.date)
                }
              }
            }
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
      ApodListScreen(modifier = Modifier.fillMaxSize()) {}
    }
  }
}