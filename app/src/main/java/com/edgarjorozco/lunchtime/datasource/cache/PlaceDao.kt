package com.edgarjorozco.lunchtime.datasource.cache

import androidx.room.*

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(placeDetail: PlaceCacheEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(placeDetails: List<PlaceCacheEntity>): List<Long>

    @Update
    suspend fun update(placeDetail: PlaceCacheEntity)

    @Update(entity = PlaceCacheEntity::class)
    suspend fun update(update: PlaceCacheUpdate)

    @Update
    suspend fun update(placeDetails: List<PlaceCacheEntity>)

    @Transaction
    suspend fun upsert(placeDetail: PlaceCacheEntity) {
        val id = insert(placeDetail)
        if (id == -1L) update(placeDetail)
    }

    @Transaction
    suspend fun upsert(placeDetails: List<PlaceCacheEntity>) {
        val ids = insert(placeDetails)
        val updateObjects = ArrayList<PlaceCacheEntity>()

        ids.forEachIndexed { index, id ->
            if (id == -1L) updateObjects.add(placeDetails[index])
        }
        update(updateObjects)
    }

    @Query("SELECT * FROM placeDetails WHERE placeId = :placeId")
    suspend fun getDetail(placeId: String): PlaceCacheEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheFavorite(placeDetail: PlaceCacheEntity): Long

    @Query("SELECT EXISTS(SELECT * FROM placeDetails WHERE placeId = :placeId AND favorite = 1)")
    suspend fun isFavorite(placeId: String): Boolean

    @Delete
    suspend fun deleteFavorite(placeDetail: PlaceCacheEntity)

    @Query("SELECT * FROM placeDetails WHERE favorite = 1")
    suspend fun getFavorites(): List<PlaceCacheEntity>

    @Query("SELECT EXISTS(SELECT * FROM placeDetails WHERE placeId = :placeId)")
    suspend fun hasPlace(placeId: String): Boolean

}