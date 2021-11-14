package com.edgarjorozco.lunchtime.util


import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.edgarjorozco.lunchtime.domain.OpenHoursPeriod
import com.edgarjorozco.lunchtime.domain.PlacePhoto
import com.edgarjorozco.lunchtime.domain.PlaceReview
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

@ProvidedTypeConverter
class TypeConverters(val moshi: Moshi) {

    private val stringListType: Type = Types.newParameterizedType(List::class.java, String::class.java)
    private val reviewListType: Type = Types.newParameterizedType(List::class.java, PlaceReview::class.java)
    private val hoursListType: Type = Types.newParameterizedType(List::class.java, OpenHoursPeriod::class.java)
    private val photosListType: Type = Types.newParameterizedType(List::class.java, PlacePhoto::class.java)

    @TypeConverter
    fun stringsFromJsonString(json: String?): List<String>? =
        if (json == null ) null else moshi.adapter<List<String>>(stringListType).fromJson(json)
    @TypeConverter
    fun stringsToJsonString(values: List<String>?): String? =
        if (values == null ) null else moshi.adapter<List<String>>(stringListType).toJson(values)

    @TypeConverter
    fun reviewsFromJsonString(json: String?): List<PlaceReview>? =
        if (json == null ) null else moshi.adapter<List<PlaceReview>>(reviewListType).fromJson(json)
    @TypeConverter
    fun reviewsToJsonString(values: List<PlaceReview>?): String? =
        if (values == null ) null else moshi.adapter<List<PlaceReview>>(reviewListType).toJson(values)

    @TypeConverter
    fun hoursFromJsonString(json: String?): List<OpenHoursPeriod>? =
        if (json == null ) null else moshi.adapter<List<OpenHoursPeriod>>(hoursListType).fromJson(json)
    @TypeConverter
    fun hoursToJsonString(values: List<OpenHoursPeriod>?): String? =
        if (values == null ) null else moshi.adapter<List<OpenHoursPeriod>>(hoursListType).toJson(values)

    @TypeConverter
    fun photosFromJsonString(json: String?): List<PlacePhoto>? =
        if (json == null ) null else moshi.adapter<List<PlacePhoto>>(photosListType).fromJson(json)
    @TypeConverter
    fun photosToJsonString(values: List<PlacePhoto>?): String? =
        if (values == null ) null else moshi.adapter<List<PlacePhoto>>(photosListType).toJson(values)
}