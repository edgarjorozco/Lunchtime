package com.edgarjorozco.lunchtime.usecases

import com.edgarjorozco.lunchtime.models.LatLng
import com.edgarjorozco.lunchtime.repository.AutoCompleteRepository
import com.edgarjorozco.lunchtime.repository.PlaceDetailRepository

class AutoCompleteUseCases (
    private val autoCompleteRepository: AutoCompleteRepository,
    private val fullDetailRepository: PlaceDetailRepository
){
    suspend fun getPlaceSuggestions(
        input: String,
        center: LatLng?
    ) = autoCompleteRepository.getPlaceSuggestions(input, center)

    suspend fun getFullPlaceDetail(placeId: String) = fullDetailRepository.getFullPlaceDetail(placeId)
}