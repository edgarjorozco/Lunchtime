package com.edgarjorozco.lunchtime.datasource.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ListApiResponse<T>(
    val status: String,
    val results: List<T>,
    val info_messages: List<String>?,
    val error_message: String?,
    val next_page_token: String?
)