package com.edgarjorozco.lunchtime.datasource.network

import com.edgarjorozco.lunchtime.util.constants.PlaceFields
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceDetailWebservice {
    @GET("details/json")
    suspend fun getPlaceDetails (
        @Query("place_id") placeId: String,
        @Query("fields") fields: String = PlaceFields.defaultFields
    ): SingleItemApiResponse<PlaceRaw>
}