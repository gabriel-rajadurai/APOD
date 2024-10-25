package com.gabriel.apod_compose.ui.commons

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
object DatePickerSelectable : SelectableDates {

  override fun isSelectableDate(utcTimeMillis: Long): Boolean {
    return utcTimeMillis <= System.currentTimeMillis()
  }

  override fun isSelectableYear(year: Int): Boolean {
    return year <= LocalDate.now().year
  }
}