package com.edgarjorozco.lunchtime.util.di

import android.content.Context
import androidx.room.Room
import com.edgarjorozco.lunchtime.datasource.cache.PlaceDao
import com.edgarjorozco.lunchtime.datasource.cache.PlaceDatabase
import com.edgarjorozco.lunchtime.util.TypeConverters
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideTypeConverters(moshi: Moshi): TypeConverters = TypeConverters(moshi)

    @Singleton
    @Provides
    fun providePlaceDetailsDatabase(@ApplicationContext appContext: Context, typeConverters: TypeConverters): PlaceDatabase {
        return Room.databaseBuilder(
            appContext,
            PlaceDatabase::class.java,
            PlaceDatabase.DATABASE_NAME
        )
            .addTypeConverter(typeConverters)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePlaceDetailsDao(placeDatabase: PlaceDatabase): PlaceDao {
        return placeDatabase.placeDao()
    }

}