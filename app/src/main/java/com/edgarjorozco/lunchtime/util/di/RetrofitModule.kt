package com.edgarjorozco.lunchtime.util.di

import android.content.Context
import com.edgarjorozco.lunchtime.BuildConfig
import com.edgarjorozco.lunchtime.R
import com.edgarjorozco.lunchtime.datasource.network.AutoCompleteWebservice
import com.edgarjorozco.lunchtime.datasource.network.NearbySearchWebservice
import com.edgarjorozco.lunchtime.datasource.network.PlaceDetailWebservice
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient  {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newUrl = chain.request().url.newBuilder()
                    .addQueryParameter("key", BuildConfig.GOOGLE_PLACES_KEY).build()
                val request = chain.request().newBuilder().url(newUrl).build()
                return@addInterceptor chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi, @ApplicationContext appContext: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(appContext.getString(R.string.google_places_api_base_url))
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun provideSearchWebservice(retrofit: Retrofit): NearbySearchWebservice = retrofit.create(
        NearbySearchWebservice::class.java)

    @Singleton
    @Provides
    fun provideDetailWebservice(retrofit: Retrofit): PlaceDetailWebservice = retrofit.create(
        PlaceDetailWebservice::class.java)

    @Singleton
    @Provides
    fun provideAutoCompleteWebservice(retrofit: Retrofit): AutoCompleteWebservice = retrofit.create(
        AutoCompleteWebservice::class.java)
}