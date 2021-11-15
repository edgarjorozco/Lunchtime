package com.edgarjorozco.lunchtime.repository

import com.edgarjorozco.lunchtime.datasource.cache.PlaceDao
import com.edgarjorozco.lunchtime.datasource.network.NearbySearchWebservice
import com.edgarjorozco.lunchtime.models.DataState
import com.edgarjorozco.lunchtime.models.LatLng
import com.edgarjorozco.lunchtime.util.constants.ApiResponseStatus
import com.edgarjorozco.lunchtime.util.mappers.PlaceNetworkMapper
import kotlinx.coroutines.flow.flow

class NearbySearchRepository
constructor(private val nearbySearchWebservice: NearbySearchWebservice,
            private val dao: PlaceDao,
            private val networkMapper: PlaceNetworkMapper
) {
    suspend fun getNearbyPlaces(center: LatLng, radius: Int) = flow {
        emit(DataState.Loading)
        try {
            val response = nearbySearchWebservice.getNearbyPlaces(center, radius)
            when(response.status) {
                ApiResponseStatus.OK -> {
                    val nearbyMap = networkMapper.mapFromDataSourceObjList(response.results)
                            .associateBy { place -> place.placeId }
                    nearbyMap.values.forEach {
                        if (dao.isFavorite(it.placeId)) it.isFavorite = true
                    }
                    emit(DataState.Success(nearbyMap))
                }
                ApiResponseStatus.NO_RESULTS -> emit(DataState.Success(null))
                else -> {
                    emit(DataState.Error(Exception(response.error_message)))
                }
            }

        } catch (ex: Exception) {
            emit(DataState.Error(ex))
        }
    }
}