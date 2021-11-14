package com.edgarjorozco.lunchtime.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlacePhoto (
    val height: Int,

    val width: Int,

    @Json(name = "photo_reference")
    val photoReference: String
)