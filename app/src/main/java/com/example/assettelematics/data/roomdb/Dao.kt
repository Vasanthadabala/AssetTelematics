package com.example.assettelematics.data.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VehicleConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vehicleConfigs: List<VehicleConfigEntity>)

    @Query("SELECT * FROM vehicle_config")
    fun getAll(): LiveData<List<VehicleConfigEntity>>
}