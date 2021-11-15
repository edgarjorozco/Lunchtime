package com.edgarjorozco.lunchtime.util.di

import com.edgarjorozco.lunchtime.repository.AutoCompleteRepository
import com.edgarjorozco.lunchtime.repository.PlaceDetailRepository
import com.edgarjorozco.lunchtime.usecases.AutoCompleteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {
    @Singleton
    @Provides
    fun provideAutoCompleteUseCases(
        autoCompleteRepository: AutoCompleteRepository,
        placeDetailRepository: PlaceDetailRepository
    ): AutoCompleteUseCases {
        return AutoCompleteUseCases(autoCompleteRepository, placeDetailRepository)
    }
}