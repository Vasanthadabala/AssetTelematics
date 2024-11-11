package com.example.assettelematics.data.ktor

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.assettelematics.data.roomdb.AppDatabase
import com.example.assettelematics.data.roomdb.VehicleConfigEntity
import kotlinx.coroutines.launch


class VehicleConfigViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "vehicle-config-database"
    ).build()

    private val vehicleConfigDao = db.vehicleConfigDao()

    fun fetchAndStoreVehicleConfig() {
        viewModelScope.launch {
            Log.d("VehicleConfigViewModel", "Fetching vehicle config")
            try {
                val response = fetchVehicleConfig()
                Log.d("VehicleConfigViewModel", "Fetched vehicle config successfully: $response")

                val maxSize = listOf(
                    response.vehicle_type.size,
                    response.vehicle_make.size,
                    response.manufacture_year.size,
                    response.fuel_type.size,
                    response.vehicle_capacity.size
                ).maxOrNull() ?: 0

                val entities = (0 until maxSize).map { index ->
                    VehicleConfigEntity(
                        id = index,
                        vehicle_type = response.vehicle_type.getOrElse(index) { VehicleType("", 0) }.text,
                        vehicle_make = response.vehicle_make.getOrElse(index) { VehicleMake("", 0) }.text,
                        manufacture_year = response.manufacture_year.getOrElse(index) { ManufactureYear("", 0) }.text,
                        fuel_type = response.fuel_type.getOrElse(index) { FuelType("", 0) }.text,
                        vehicle_capacity = response.vehicle_capacity.getOrElse(index) { VehicleCapacity("", 0) }.text
                    )
                }

                vehicleConfigDao.insertAll(entities)
                Log.d("VehicleConfigViewModel", "Inserted vehicle config successfully")
            } catch (e: Exception) {
                Log.e("VehicleConfigViewModel", "Error fetching or inserting data", e)
            }
        }
    }

    fun getVehicleConfigs(): LiveData<List<VehicleConfigEntity>> {
        return vehicleConfigDao.getAll()
    }
}
