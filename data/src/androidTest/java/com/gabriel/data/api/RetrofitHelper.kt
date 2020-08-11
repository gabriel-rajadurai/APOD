package com.gabriel.data.api

val apodService by lazy { APODRetrofitHelper().getApodNetworkService() }