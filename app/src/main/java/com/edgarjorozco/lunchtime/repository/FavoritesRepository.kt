package com.edgarjorozco.lunchtime.repository

import com.edgarjorozco.lunchtime.datasource.cache.PlaceDao
import com.edgarjorozco.lunchtime.models.DataState
import com.edgarjorozco.lunchtime.models.Place
import com.edgarjorozco.lunchtime.util.mappers.PlaceCacheMapper
import kotlinx.coroutines.flow.flow

class FavoritesRepository
constructor(
    private val dao: PlaceDao,
    private val cacheMapper: PlaceCacheMapper
) {
    suspend fun favorite(place: Place, isFavorite: Boolean) {
        if (isFavorite) dao.cacheFavorite(cacheMapper.mapToDataSourceObj(place))
        else dao.deleteFavorite(cacheMapper.mapToDataSourceObj(place))
    }

    suspend fun getFavorites() = flow {
        emit(DataState.Loading)
        try {
            val favoriteList = cacheMapper.mapFromDataSourceObjList(dao.getFavorites())
            emit(DataState.Success(favoriteList.associateBy { place -> place.placeId } ))
        } catch (ex: Exception) {
            emit(DataState.Error(ex))
        }
    }
}