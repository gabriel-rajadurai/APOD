package com.gabriel.apod_compose.ui.screens.apodlist

sealed interface Action {
  data object View : Action
  data object Share : Action
  data object Download : Action
}