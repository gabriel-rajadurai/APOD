package com.gabriel.data.dagger.modules

import com.gabriel.data.api.APODNetworkService
import com.gabriel.data.api.RequestInteceptor
import com.gabriel.data.datasources.defs.APODDataSourceDef
import com.gabriel.data.datasources.impl.remote.APODRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class APODNetworkModule {

  @Provides
  fun providesGsonConverterFactory(): GsonConverterFactory {
    return GsonConverterFactory.create()
  }

  @Provides
  fun providesRequestInterceptor(): RequestInteceptor {
    return RequestInteceptor()
  }

  @Singleton
  @Provides
  fun provideHttpClient(
    requestInterceptor: RequestInteceptor
  ): OkHttpClient {
    return OkHttpClient().newBuilder()
      .addInterceptor(HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
      })
      .addInterceptor(requestInterceptor)
      .build()
  }

  @Singleton
  @Provides
  fun provideRetrofit(
    converterFactory: GsonConverterFactory,
    httpClient: OkHttpClient
  ): Retrofit {
    return Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(converterFactory)
      .client(httpClient)
      .build()
  }

  @Singleton
  @Provides
  fun provideApodNetworkService(retrofit: Retrofit): APODNetworkService {
    return retrofit.create(APODNetworkService::class.java)
  }

  @RemoteSource
  @Singleton
  @Provides
  fun provideApodRemoteDataSource(apodService: APODNetworkService): APODDataSourceDef {
    return APODRemoteDataSource((apodService))
  }


  companion object {
    private const val BASE_URL = "https://api.nasa.gov/planetary/"
  }

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteSource
