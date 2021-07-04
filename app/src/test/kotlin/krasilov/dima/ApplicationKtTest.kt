package krasilov.dima

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import krasilov.dima.web.BookingHistoryRecord
import krasilov.dima.web.DeviceInfoExtended
import krasilov.dima.web.GenericResponse
import krasilov.dima.web.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

// fixme real db and testcontainers https://www.youtube.com/watch?v=gF-YG6YZxZk
class ApplicationKtTest {

    @Test
    fun testGetDevices() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/devices").apply {
                val (devices, _) = Json.decodeFromString<GenericResponse<List<DeviceInfoExtended>>>(response.content!!)
                assertEquals(10, devices!!.size)
                assertEquals(HttpStatusCode.OK, response.status())
            }

            handleRequest(HttpMethod.Get, "/users").apply {
                val users = Json.decodeFromString<List<User>>(response.content!!)
                assertEquals(4, users.size)
                assertEquals(HttpStatusCode.OK, response.status())
            }

            // successful booking
            handleRequest(HttpMethod.Post, "/bookings") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(""" { "userId" : 1, "deviceId" : 1 } """)
            }
                .apply {
                    val (device, _) = Json.decodeFromString<GenericResponse<DeviceInfoExtended>>(response.content!!)
                    assertEquals(HttpStatusCode.OK, response.status())
                }

            // already booked
            handleRequest(HttpMethod.Post, "/bookings") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(""" { "userId" : 1, "deviceId" : 1 } """)
            }
                .apply {
                    val (device, _) = Json.decodeFromString<GenericResponse<DeviceInfoExtended>>(response.content!!)
                    assertEquals(418, response.status()?.value)
                }

            handleRequest(HttpMethod.Delete, "/bookings") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(""" { "userId" : 1, "deviceId" : 1 } """)
            }
                .apply {
                    val (device, _) = Json.decodeFromString<GenericResponse<DeviceInfoExtended>>(response.content!!)
                    assertEquals(HttpStatusCode.OK, response.status())
                }

            // not booked
            handleRequest(HttpMethod.Delete, "/bookings") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(""" { "userId" : 1, "deviceId" : 1 } """)
            }
                .apply {
                    val (device, _) = Json.decodeFromString<GenericResponse<DeviceInfoExtended>>(response.content!!)
                    assertEquals(418, response.status()?.value)
                }

            handleRequest(HttpMethod.Get, "/bookings")
                .apply {
                    val history = Json.decodeFromString<List<BookingHistoryRecord>>(response.content!!)
                    assertEquals(1, history.size)
                    assertEquals(HttpStatusCode.OK, response.status())
                }

        }

    }

}