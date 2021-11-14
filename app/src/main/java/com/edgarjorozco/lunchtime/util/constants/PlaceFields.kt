package com.edgarjorozco.lunchtime.util.constants

object PlaceFields {
    const val ADDRESS_COMPONENT = "address_component"
    const val ADR_ADDRESS = "adr_address"
    const val BUSINESS_STATUS = "business_status"
    const val FORMATTED_ADDRESS = "formatted_address"
    const val GEOMETRY = "geometry"
    const val ICON = "icon"
    const val ICON_MASK_BASE_URI = "icon_mask_base_uri"
    const val ICON_BACKGROUND_COLOR = "icon_background_color"
    const val NAME = "name"
    const val PERMANENTLY_CLOSED = "permanently_closed"
    const val PHOTO = "photo"
    const val PLACE_ID = "place_id"
    const val PLUS_CODE = "plus_code"
    const val TYPE = "type"
    const val MAPS_URL = "url"
    const val UTC_OFFSET = "utc_offset"
    const val VICINITY = "vicinity"
    const val FORMATTED_PHONE_NUMBER = "formatted_phone_number"
    const val INTERNATIONAL_PHONE_NUMBER = "international_phone_number"
    const val OPENING_HOURS = "opening_hours"
    const val WEBSITE = "website"
    const val PRICE_LEVEL = "price_level"
    const val RATING = "rating"
    const val REVIEWS = "reviews"
    const val USER_RATINGS_TOTAL = "user_ratings_total"

    const val defaultFields =
        "$PLACE_ID,$FORMATTED_ADDRESS,$PHOTO,$FORMATTED_PHONE_NUMBER,$OPENING_HOURS,$WEBSITE,$REVIEWS,$MAPS_URL,$GEOMETRY"
    const val fullPlaceDetail =
        "$BUSINESS_STATUS,$NAME,$TYPE,$PRICE_LEVEL,$RATING,$USER_RATINGS_TOTAL,$PLACE_ID,$FORMATTED_ADDRESS,$PHOTO,$FORMATTED_PHONE_NUMBER,$OPENING_HOURS,$WEBSITE,$REVIEWS,$MAPS_URL,$GEOMETRY,$VICINITY"
}