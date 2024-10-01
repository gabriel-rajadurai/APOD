package com.gabriel.apod_compose.commons

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gabriel.apod_compose.R
import kotlinx.coroutines.launch

@Composable
@Preview
fun LoadingIndicator(
  modifier: Modifier = Modifier
) {

  val scope = rememberCoroutineScope()
  // Allow resume on rotation
  var currentRotation1 by remember { mutableFloatStateOf(0f) }
  var currentRotation2 by remember { mutableFloatStateOf(0f) }
  var currentRotation3 by remember { mutableFloatStateOf(0f) }

  val rotation = remember { Animatable(currentRotation1) }
  val rotation2 = remember { Animatable(currentRotation1) }
  val rotation3 = remember { Animatable(currentRotation1) }


  suspend fun animateIcon1(onComplete: suspend () -> Unit) {
    rotation.animateTo(
      currentRotation1 + 360f,
      animationSpec = repeatable(
        iterations = 1,
        animation = tween(1000, easing = LinearEasing),
        repeatMode = RepeatMode.Restart
      )
    ) {
      currentRotation1 = value
      if (currentRotation1 == targetValue) {
        scope.launch {
          onComplete()
        }
      }
    }
  }

  suspend fun animateIcon2(onComplete: suspend () -> Unit) {
    rotation2.animateTo(
      currentRotation2 + 360f,
      animationSpec = repeatable(
        iterations = 1,
        animation = tween(1000, easing = LinearEasing),
        repeatMode = RepeatMode.Restart
      )
    ) {
      currentRotation2 = value
      if (currentRotation2 == targetValue) {
        scope.launch {
          onComplete()
        }
      }
    }
  }

  suspend fun animateIcon3(onComplete: suspend () -> Unit) {
    rotation3.animateTo(
      currentRotation3 + 360f,
      animationSpec = repeatable(
        iterations = 1,
        animation = tween(1000, easing = LinearEasing),
        repeatMode = RepeatMode.Restart
      )
    ) {
      currentRotation3 = value
      if (currentRotation3 == targetValue) {
        scope.launch {
          onComplete()
        }
      }
    }
  }

  suspend fun startLoadingAnimation() {
    animateIcon3 {
      animateIcon1 {
        animateIcon2 {
          startLoadingAnimation()
        }
      }
    }
  }


  LaunchedEffect(key1 = Unit) {
    startLoadingAnimation()
  }


  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(5.dp)
  ) {
    Image(
      imageVector = ImageVector.vectorResource(id = R.drawable.ic_shape3),
      contentDescription = "",
      modifier = Modifier.rotate(currentRotation1)
    )

    Row(
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Image(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_shape1),
        contentDescription = "",
        modifier = Modifier.rotate(currentRotation2)
      )

      Image(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_shape2),
        contentDescription = "",
        modifier = Modifier.rotate(currentRotation3)
      )
    }
  }

}