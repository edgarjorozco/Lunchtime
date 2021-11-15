package com.edgarjorozco.lunchtime.util.mappers

import com.edgarjorozco.lunchtime.datasource.cache.PlaceCacheUpdate
import com.edgarjorozco.lunchtime.models.Place
import javax.inject.Inject

class PlaceUpdateCacheMapper @Inject constructor() : DataSourceObjectMapper<PlaceCacheUpdate, Place>() {
    override fun mapFromDataSourceObj(dataSourceObject: PlaceCacheUpdate): Place {
        return Place(
            placeId = dataSourceObject.placeId,
            fullAddress = dataSourceObject.fullAddress,
            formattedPhoneNumber = dataSourceObject.formattedPhoneNumber,
            openHours = dataSourceObject.openHours,
            photos = dataSourceObject.photos,
            reviews = dataSourceObject.reviews,
            googleMapsUrl = dataSourceObject.googleMapsUrl,
            website = dataSourceObject.website,
            location = dataSourceObject.location,
            bounds = dataSourceObject.bounds
        )
    }

    override fun mapToDataSourceObj(domainModel: Place): PlaceCacheUpdate {
        return PlaceCacheUpdate(
            placeId = domainModel.placeId,
            fullAddress = domainModel.fullAddress,
            formattedPhoneNumber = domainModel.formattedPhoneNumber,
            openHours = domainModel.openHours,
            photos = domainModel.photos,
            reviews = domainModel.reviews,
            googleMapsUrl = domainModel.googleMapsUrl,
            website = domainModel.website,
            location = domainModel.location,
            bounds = domainModel.bounds
        )
    }
}