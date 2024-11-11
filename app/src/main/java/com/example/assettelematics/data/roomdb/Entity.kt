package com.example.assettelematics.data.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicle_config")
data class VehicleConfigEntity(
    @PrimaryKey val id: Int,
    val vehicle_type: String,
    val vehicle_make: String,
    val manufacture_year: String,
    val fuel_type: String,
    val vehicle_capacity: String
)