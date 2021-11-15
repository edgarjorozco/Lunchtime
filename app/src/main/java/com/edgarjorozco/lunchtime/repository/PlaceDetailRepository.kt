package com.edgarjorozco.lunchtime.repository

import com.edgarjorozco.lunchtime.datasource.cache.PlaceDao
import com.edgarjorozco.lunchtime.datasource.network.PlaceDetailWebservice
import com.edgarjorozco.lunchtime.models.DataState
import com.edgarjorozco.lunchtime.models.Place
import com.edgarjorozco.lunchtime.util.constants.ApiResponseStatus
import com.edgarjorozco.lunchtime.util.constants.PlaceFields
import com.edgarjorozco.lunchtime.util.mappers.PlaceCacheMapper
import com.edgarjorozco.lunchtime.util.mappers.PlaceNetworkMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaceDetailRepository
constructor(
    private val detailWebservice: PlaceDetailWebservice,
    private val dao: PlaceDao,
    private val networkMapper: PlaceNetworkMapper,
    private val cacheMapper: PlaceCacheMapper
){
    suspend fun getFullPlaceDetail(placeId: String): Flow<DataState<Place>> = flow {
        emit(DataState.Loading)
        try {
            if (dao.hasPlace(placeId)) {
                val place = cacheMapper.mapFromDataSourceObj(dao.getDetail(placeId))
                emit(DataState.Success(place))
            }
            else {
                val response = detailWebservice.getPlaceDetails(placeId, PlaceFields.fullPlaceDetail)
                when (response.status) {
                    ApiResponseStatus.OK -> {
                        val place = networkMapper.mapFromDataSourceObj(response.result)
                        dao.upsert(cacheMapper.mapToDataSourceObj(place))
                        emit(DataState.Success(place))
                    }
                    else -> {
                        emit(DataState.Error(Exception(response.error_message)))
                    }
                }
            }

        } catch (ex: Exception) {
            emit(DataState.Error(ex))
        }
    }
}