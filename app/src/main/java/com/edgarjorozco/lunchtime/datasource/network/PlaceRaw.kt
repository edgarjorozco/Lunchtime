package com.edgarjorozco.lunchtime.datasource.network

import com.edgarjorozco.lunchtime.models.*
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaceRaw (
    val business_status: String?,
    val formatted_address: String?,
    val formatted_phone_number: String?,
    val geometry: GeometryRaw,
    val name: String?,
    val opening_hours: OpeningHoursRaw?,
    val photos: List<PlacePhoto>?,
    val place_id: String,
    val price_level: Int?,
    val rating: Float?,
    val reviews: List<PlaceReview>?,
    val types: List<String>?,
    val url: String?,
    val user_ratings_total: Int?,
    val vicinity: String?,
    val website: String?
)

@JsonClass(generateAdapter = true)
data class OpeningHoursRaw (
    val periods: List<OpenHoursPeriod>?
)

@JsonClass(generateAdapter = true)
data class GeometryRaw (
    val location: LatLng,
    val viewport: LatLngBounds
)