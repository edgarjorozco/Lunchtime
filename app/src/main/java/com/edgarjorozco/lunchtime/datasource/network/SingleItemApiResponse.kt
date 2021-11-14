package com.edgarjorozco.lunchtime.datasource.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SingleItemApiResponse<T>(
    val status: String,
    val result: T,
    val info_messages: List<String>?,
    val error_message: String?
)