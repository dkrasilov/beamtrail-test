package krasilov.dima.web

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import krasilov.dima.integration.FonoApiDeviceInfo

@Serializable
data class GenericResponse<T>(
    val result: T? = null,
    val error: Error? = null
) {
    companion object {
        fun <T> success(t: T) = GenericResponse(result = t)
        fun error(code: ErrorCode, message: String): GenericResponse<Unit> =
            GenericResponse(error = Error(code, message))

        fun error(error: Error): GenericResponse<Unit> = GenericResponse(error = error)
    }
}

@Serializable
data class Error(
    val code: ErrorCode,
    val message: String,
    val statusCode: Int = 418
)

enum class ErrorCode {
    BT00001,
    BT00002,
    BT00003
}

@Serializable
data class User(val id: Int, val name: String)

@Serializable
data class Device(val id: Int, val name: String)

@Serializable
data class DeviceInfo(
    val id: Int,
    val name: String,
    val available: Boolean,
    val booking: DeviceBooking? = null
)

@Serializable
data class DeviceInfoExtended(
    val device: DeviceInfo,
    val fonoInfo: FonoApiDeviceInfo? = null
)

@Serializable
data class DeviceBooking(
    val id: Int,
    val user: User,
    val bookedAt: LocalDateTime,
    val returnedAt: LocalDateTime?
)

@Serializable
data class BookingHistoryRecord(
    val id: Int,
    val device: Device,
    val user: User,
    val bookedAt: LocalDateTime,
    val returnedAt: LocalDateTime?
)