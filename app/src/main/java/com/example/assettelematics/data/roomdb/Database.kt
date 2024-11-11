package com.example.assettelematics.data.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [VehicleConfigEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vehicleConfigDao(): VehicleConfigDao
}