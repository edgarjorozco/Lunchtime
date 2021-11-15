package com.edgarjorozco.lunchtime.repository

import com.edgarjorozco.lunchtime.datasource.network.AutoCompleteWebservice
import com.edgarjorozco.lunchtime.models.DataState
import com.edgarjorozco.lunchtime.models.LatLng
import com.edgarjorozco.lunchtime.util.constants.ApiResponseStatus
import kotlinx.coroutines.flow.flow

class AutoCompleteRepository
constructor(
    private val autoCompleteWebservice: AutoCompleteWebservice
) {


    suspend fun getPlaceSuggestions(input: String, center: LatLng?) = flow {
        emit(DataState.Loading)
        try {
            val response = autoCompleteWebservice.autoCompleteSearch(input,center)
            when (response.status) {
                ApiResponseStatus.OK, ApiResponseStatus.NO_RESULTS ->
                    emit(DataState.Success(ArrayList(response.predictions)))
                else -> emit(DataState.Error(Exception(response.error_message)))
            }
        } catch (ex: Exception) {
            emit(DataState.Error(ex))
        }
    }
}