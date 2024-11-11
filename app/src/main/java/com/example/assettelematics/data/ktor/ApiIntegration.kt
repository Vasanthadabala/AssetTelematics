package com.example.assettelematics.data.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


private val client = HttpClient(Android) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = true
        })
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
}


suspend fun fetchVehicleConfig(): VehicleConfigResponse {

    val requestData = VehicleConfigRequest(
        clientid = 11,
        enterprise_code = 1007,
        mno = "9889897789",
        passcode = 3476
    )

    return client.post("http://103.123.173.50:8080/jhsmobileapi/mobile/configure/v1/task") {
        contentType(ContentType.Application.Json)
        setBody(requestData)
    }.body()

}

@Serializable
data class VehicleConfigRequest(
    val clientid: Int,
    val enterprise_code: Int,
    val mno: String,
    val passcode: Int
)