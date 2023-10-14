package com.gabriel.astronomypod.common

import androidx.lifecycle.ViewModel
import android.content.Context
import androidx.annotation.StringRes

open class BaseViewModel(private val app: Context) : ViewModel(){

    protected fun getString(@StringRes res: Int) = app.getString(res)
}