package com.edgarjorozco.lunchtime.util.mappers

import com.edgarjorozco.lunchtime.datasource.cache.PlaceCacheEntity
import com.edgarjorozco.lunchtime.domain.Place
import javax.inject.Inject

class PlaceCacheMapper @Inject constructor() : DataSourceObjectMapper<PlaceCacheEntity, Place>() {
    override fun mapFromDataSourceObj(dataSourceObject: PlaceCacheEntity): Place {
        return Place(
            placeId = dataSourceObject.placeId,
            name = dataSourceObject.name,
            businessStatus = dataSourceObject.businessStatus,
            fullAddress = dataSourceObject.fullAddress,
            simplifiedAddress = dataSourceObject.simplifiedAddress,
            formattedPhoneNumber = dataSourceObject.formattedPhoneNumber,
            openHours = dataSourceObject.openHours,
            photos = dataSourceObject.photos,
            priceLevel = dataSourceObject.priceLevel,
            rating = dataSourceObject.rating,
            reviews = dataSourceObject.reviews,
            placeTypes = dataSourceObject.placeTypes,
            googleMapsUrl = dataSourceObject.googleMapsUrl,
            ratingsCount = dataSourceObject.ratingsCount,
            website = dataSourceObject.website,
            location = dataSourceObject.location,
            bounds = dataSourceObject.bounds,
            isFavorite = dataSourceObject.favorite
        )
    }

    override fun mapToDataSourceObj(domainModel: Place): PlaceCacheEntity {
        return PlaceCacheEntity(
            placeId = domainModel.placeId,
            name = domainModel.name,
            businessStatus = domainModel.businessStatus,
            fullAddress = domainModel.fullAddress,
            simplifiedAddress = domainModel.simplifiedAddress,
            formattedPhoneNumber = domainModel.formattedPhoneNumber,
            openHours = domainModel.openHours,
            photos = domainModel.photos,
            priceLevel = domainModel.priceLevel,
            rating = domainModel.rating,
            reviews = domainModel.reviews,
            placeTypes = domainModel.placeTypes,
            googleMapsUrl = domainModel.googleMapsUrl,
            ratingsCount = domainModel.ratingsCount,
            website = domainModel.website,
            location = domainModel.location,
            bounds = domainModel.bounds,
            favorite = domainModel.isFavorite
        )
    }
}