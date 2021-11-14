package com.edgarjorozco.lunchtime.datasource.network

import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)

data class AutoCompleteResponse (
    val status: String,
    val predictions: List<AutoCompletePrediction>,
    val info_messages: List<String>?,
    val error_message: String?
    )