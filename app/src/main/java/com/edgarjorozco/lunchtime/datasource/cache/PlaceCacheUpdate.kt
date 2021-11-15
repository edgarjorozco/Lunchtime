package com.edgarjorozco.lunchtime.datasource.cache

import androidx.room.Embedded
import com.edgarjorozco.lunchtime.models.*

data class PlaceCacheUpdate (
    val placeId: String,
    val fullAddress: String?,
    val formattedPhoneNumber: String?,
    val openHours: List<OpenHoursPeriod>?,
    val photos: List<PlacePhoto>?,
    val reviews: List<PlaceReview>?,
    val googleMapsUrl: String?,
    val website: String?,
    @Embedded(prefix = "loc_")
    val location: LatLng,
    @Embedded(prefix = "bounds_")
    val bounds: LatLngBounds
    )