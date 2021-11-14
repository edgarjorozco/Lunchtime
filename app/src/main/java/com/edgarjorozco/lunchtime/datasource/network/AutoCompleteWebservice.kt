package com.edgarjorozco.lunchtime.datasource.network

import com.edgarjorozco.lunchtime.domain.LatLng
import com.edgarjorozco.lunchtime.util.constants.PlaceTypes
import retrofit2.http.GET
import retrofit2.http.Query

interface AutoCompleteWebservice {
    @GET("autocomplete/json")
    suspend fun autoCompleteSearch (
        @Query("input") inputString: String,
        @Query("location") center: LatLng?,
        @Query("types") placeType: String = PlaceTypes.ESTABLISHMENT
    ): AutoCompleteResponse

}