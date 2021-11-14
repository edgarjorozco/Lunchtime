package com.edgarjorozco.lunchtime.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaceReview(
    @Json(name = "author_name")
    val authorName: String,

    val rating: Int,

    @Json(name = "relative_time_description")
    val relativeTime: String,

    val time: Long,

    @Json(name = "author_url")
    val authorUrl: String?,

    @Json(name = "profile_photo_url")
    val authorPhotoUrl: String?,

    val text: String?
)