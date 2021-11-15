package com.edgarjorozco.lunchtime.models

import androidx.room.Embedded
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LatLng (
    val lat: Double,

    val lng: Double
) {
    override fun toString(): String = "$lat,$lng"
}

@JsonClass(generateAdapter = true)
data class LatLngBounds (
    @Embedded(prefix = "northeast_")
    val northeast: LatLng,

    @Embedded(prefix = "southwest_")
    val southwest: LatLng
)