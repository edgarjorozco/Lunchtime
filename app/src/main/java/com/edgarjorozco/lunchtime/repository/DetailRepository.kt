package com.edgarjorozco.lunchtime.repository

import com.edgarjorozco.lunchtime.models.DataState
import com.edgarjorozco.lunchtime.models.Place
import kotlinx.coroutines.flow.Flow

interface DetailRepository {
    suspend fun getFullPlaceDetail(placeId: String): Flow<DataState<Place>>
}