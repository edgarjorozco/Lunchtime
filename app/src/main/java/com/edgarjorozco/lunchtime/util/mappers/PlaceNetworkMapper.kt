package com.edgarjorozco.lunchtime.util.mappers

import com.edgarjorozco.lunchtime.datasource.network.GeometryRaw
import com.edgarjorozco.lunchtime.datasource.network.OpeningHoursRaw
import com.edgarjorozco.lunchtime.datasource.network.PlaceRaw
import com.edgarjorozco.lunchtime.domain.Place
import javax.inject.Inject

class PlaceNetworkMapper @Inject constructor() : DataSourceObjectMapper<PlaceRaw, Place>() {
    override fun mapFromDataSourceObj(dataSourceObject: PlaceRaw): Place {
        return Place(
            placeId = dataSourceObject.place_id,
            name = dataSourceObject.name,
            businessStatus = dataSourceObject.business_status,
            fullAddress = dataSourceObject.formatted_address,
            simplifiedAddress = dataSourceObject.vicinity,
            formattedPhoneNumber = dataSourceObject.formatted_phone_number,
            openHours = dataSourceObject.opening_hours?.periods,
            photos = dataSourceObject.photos,
            priceLevel = dataSourceObject.price_level,
            rating = dataSourceObject.rating,
            reviews = dataSourceObject.reviews,
            placeTypes = dataSourceObject.types,
            googleMapsUrl = dataSourceObject.url,
            ratingsCount = dataSourceObject.user_ratings_total,
            website = dataSourceObject.website,
            location = dataSourceObject.geometry.location,
            bounds = dataSourceObject.geometry.viewport
        )
    }

    override fun mapToDataSourceObj(domainModel: Place): PlaceRaw {
        return PlaceRaw(
            place_id = domainModel.placeId,
            name = domainModel.name,
            business_status = domainModel.businessStatus,
            formatted_address = domainModel.fullAddress,
            vicinity = domainModel.simplifiedAddress,
            formatted_phone_number = domainModel.formattedPhoneNumber,
            opening_hours =
                if (domainModel.openHours != null) OpeningHoursRaw(domainModel.openHours)
                else null,
            photos = domainModel.photos,
            price_level = domainModel.priceLevel,
            rating = domainModel.rating,
            reviews = domainModel.reviews,
            types = domainModel.placeTypes,
            url = domainModel.googleMapsUrl,
            user_ratings_total = domainModel.ratingsCount,
            website = domainModel.website,
            geometry= GeometryRaw(domainModel.location, domainModel.bounds)
        )
    }
}