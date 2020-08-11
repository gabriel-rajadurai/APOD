package com.gabriel.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class APODRetrofitHelper {

    fun getApodNetworkService(): APODNetworkService {
        val httpClient = OkHttpClient().newBuilder()
            .addInterceptor(RequestInteceptor())
            .build()
        return Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/planetary/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build().create(APODNetworkService::class.java)
    }

}

class RequestInteceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url
        val newUrl = url.newBuilder()
            .addQueryParameter("api_key", "wRhfrDUOQf53z2UvpadrP3qmNNhSxx0Wjlv5HhFQ")
            .build()
        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newRequest)
    }

}