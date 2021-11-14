package com.edgarjorozco.lunchtime.util.di

import com.edgarjorozco.lunchtime.datasource.cache.PlaceDao
import com.edgarjorozco.lunchtime.datasource.network.AutoCompleteWebservice
import com.edgarjorozco.lunchtime.datasource.network.NearbySearchWebservice
import com.edgarjorozco.lunchtime.datasource.network.PlaceDetailWebservice
import com.edgarjorozco.lunchtime.repository.AutoCompleteRepository
import com.edgarjorozco.lunchtime.repository.FavoritesRepository
import com.edgarjorozco.lunchtime.repository.NearbySearchRepository
import com.edgarjorozco.lunchtime.util.mappers.PlaceCacheMapper
import com.edgarjorozco.lunchtime.util.mappers.PlaceNetworkMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideAutoCompleteRepository(
        placeDetailWebservice: PlaceDetailWebservice,
        autoCompleteWebservice: AutoCompleteWebservice,
        dao: PlaceDao,
        networkMapper: PlaceNetworkMapper,
        cacheMapper: PlaceCacheMapper
    ): AutoCompleteRepository {
        return AutoCompleteRepository(placeDetailWebservice,autoCompleteWebservice, dao, networkMapper, cacheMapper)
    }

    @Singleton
    @Provides
    fun provideFavoritesRepository(
        dao: PlaceDao,
        cacheMapper: PlaceCacheMapper
    ): FavoritesRepository {
        return FavoritesRepository(dao, cacheMapper)
    }

    @Singleton
    @Provides
    fun provideNearbySearchRepository(
        nearbySearchWebservice: NearbySearchWebservice,
        dao: PlaceDao,
        networkMapper: PlaceNetworkMapper,
    ): NearbySearchRepository {
        return NearbySearchRepository(nearbySearchWebservice, dao, networkMapper)
    }
}