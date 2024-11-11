package com.example.assettelematics.data.ktor

import kotlinx.serialization.Serializable

@Serializable
data class VehicleConfigResponse(
    val vehicle_type: List<VehicleType>,
    val vehicle_make: List<VehicleMake>,
    val manufacture_year: List<ManufactureYear>,
    val fuel_type: List<FuelType>,
    val vehicle_capacity: List<VehicleCapacity>
)

@Serializable
data class VehicleType(
    val text: String,
    val value: Int,
    val images: String? = null
)

@Serializable
data class VehicleMake(
    val text: String,
    val value: Int,
    val images: String? = null
)

@Serializable
data class ManufactureYear(
    val text: String,
    val value: Int,
    val images: String? = null
)

@Serializable
data class FuelType(
    val text: String,
    val value: Int,
    val images: String? = null
)

@Serializable
data class VehicleCapacity(
    val text: String,
    val value: Int,
    val images: String? = null
)