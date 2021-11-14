package com.edgarjorozco.lunchtime.datasource.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [PlaceCacheEntity::class], version = 2)
@TypeConverters(com.edgarjorozco.lunchtime.util.TypeConverters::class)
abstract class PlaceDatabase: RoomDatabase() {

    abstract fun placeDao(): PlaceDao

    companion object {
        const val DATABASE_NAME = "place_details_db"
    }

}