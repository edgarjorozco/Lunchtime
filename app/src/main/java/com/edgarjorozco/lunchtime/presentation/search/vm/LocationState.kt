package com.edgarjorozco.lunchtime.presentation.search.vm

import com.edgarjorozco.lunchtime.domain.LatLng

sealed class LocationState{
    data class Precise(val latLng: LatLng): LocationState()
    data class Coarse(val latLng: LatLng): LocationState()
    object None: LocationState()

    fun toData(): LatLng? = when (this) {
        is Precise -> this.latLng
        is Coarse -> this.latLng
        is None -> null
    }
}