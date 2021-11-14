package com.edgarjorozco.lunchtime.datasource.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AutoCompletePrediction (
    val description: String,
    val place_id: String
){
    override fun toString(): String = description
}