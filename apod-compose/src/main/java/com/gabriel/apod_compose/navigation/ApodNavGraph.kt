package com.gabriel.apod_compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gabriel.apod_compose.ui.screens.apodlist.ApodListScreen
import com.gabriel.apod_compose.ui.screens.today.TodayApodScreen
import com.gabriel.apod_compose.ui.screens.viewapod.ViewApodScreen
import kotlinx.serialization.Serializable

@Composable
fun ApodNavGraph(
  modifier: Modifier = Modifier
) {
  val navController = rememberNavController()
  NavHost(navController = navController, startDestination = Screen.TodayApod) {
    composable<Screen.TodayApod> {
      TodayApodScreen(modifier = modifier) {
        navController.navigate(Screen.ApodList)
      }
    }
    composable<Screen.ApodList> {
      ApodListScreen(modifier = modifier) { date ->
        navController.navigate(Screen.ViewApod(date))
      }
    }
    composable<Screen.ViewApod> {
      ViewApodScreen(modifier = modifier)
    }
  }
}


sealed interface Screen {
  @Serializable
  data object TodayApod

  @Serializable
  data object ApodList

  @Serializable
  data class ViewApod(
    val podDate: String = "{podDate}"
  )
}


