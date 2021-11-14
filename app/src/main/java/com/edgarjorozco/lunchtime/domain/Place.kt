package com.edgarjorozco.lunchtime.domain

data class Place (
    val placeId: String,
    val name: String? = null,
    val businessStatus: String? = null,
    val fullAddress: String? = null,
    val simplifiedAddress: String? = null,
    val formattedPhoneNumber: String? = null,
    val openHours: List<OpenHoursPeriod>? = null,
    val photos: List<PlacePhoto>? = null,
    val priceLevel: Int? = null,
    val rating: Float? = null,
    val reviews: List<PlaceReview>? = null,
    val placeTypes: List<String>? = null,
    val googleMapsUrl: String? = null,
    val ratingsCount: Int? = null,
    val website: String? = null,
    val location: LatLng,
    val bounds: LatLngBounds,
    var isFavorite: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Place

        if (placeId != other.placeId) return false
        if (name != other.name) return false
        if (businessStatus != other.businessStatus) return false
        if (fullAddress != other.fullAddress) return false
        if (simplifiedAddress != other.simplifiedAddress) return false
        if (formattedPhoneNumber != other.formattedPhoneNumber) return false
        if (openHours != other.openHours) return false
        if (photos != other.photos) return false
        if (priceLevel != other.priceLevel) return false
        if (rating != other.rating) return false
        if (reviews != other.reviews) return false
        if (placeTypes != other.placeTypes) return false
        if (googleMapsUrl != other.googleMapsUrl) return false
        if (ratingsCount != other.ratingsCount) return false
        if (website != other.website) return false
        if (location != other.location) return false
        if (bounds != other.bounds) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
