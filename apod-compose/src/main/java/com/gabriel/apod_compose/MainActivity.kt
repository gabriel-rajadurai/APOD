package com.gabriel.apod_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.gabriel.apod_compose.navigation.ApodNavGraph
import com.gabriel.apod_compose.ui.screens.apodlist.ApodListScreen
import com.gabriel.apod_compose.ui.screens.today.TodayApodScreen
import com.gabriel.apod_compose.ui.screens.viewapod.ViewApodScreen
import com.gabriel.apod_compose.ui.theme.AstronomyPODTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    val windowInsetsController =
      WindowCompat.getInsetsController(window, window.decorView)
    // Configure the behavior of the hidden system bars.
    windowInsetsController.systemBarsBehavior =
      WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    windowInsetsController.hide(
      WindowInsetsCompat.Type.systemBars()
    )

    setContent {
      AstronomyPODTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          ApodNavGraph(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
          )
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  AstronomyPODTheme {
    Greeting("Android")
  }
}