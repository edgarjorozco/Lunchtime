package com.edgarjorozco.lunchtime.datasource.cache

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.edgarjorozco.lunchtime.models.*


@Entity(tableName = "placeDetails")
data class PlaceCacheEntity (
    @PrimaryKey(autoGenerate = false)
    val placeId: String,
    val name: String?,
    val businessStatus: String?,
    val fullAddress: String?,
    val simplifiedAddress: String?,
    val formattedPhoneNumber: String?,
    val openHours: List<OpenHoursPeriod>?,
    val photos: List<PlacePhoto>?,
    val priceLevel: Int?,
    val rating: Float?,
    val reviews: List<PlaceReview>?,
    val placeTypes: List<String>?,
    val googleMapsUrl: String?,
    val ratingsCount: Int?,
    val website: String?,
    @Embedded(prefix = "loc_")
    val location: LatLng,
    @Embedded(prefix = "bounds_")
    val bounds: LatLngBounds,
    val favorite: Boolean = false
)