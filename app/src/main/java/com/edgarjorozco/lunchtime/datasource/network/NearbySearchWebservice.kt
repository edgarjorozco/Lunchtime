package com.edgarjorozco.lunchtime.datasource.network

import com.edgarjorozco.lunchtime.domain.LatLng
import com.edgarjorozco.lunchtime.util.constants.PlaceTypes
import retrofit2.http.GET
import retrofit2.http.Query

interface NearbySearchWebservice {

    @GET("nearbysearch/json")
    suspend fun getNearbyPlaces (
        @Query("location") center: LatLng,
        @Query("radius") radius: Int,
        @Query("type") placeType: String = PlaceTypes.RESTAURANT
    ): ListApiResponse<PlaceRaw>
}