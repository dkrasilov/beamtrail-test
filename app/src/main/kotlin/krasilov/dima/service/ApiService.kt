package krasilov.dima.service

import arrow.core.Either
import kotlinx.datetime.*
import krasilov.dima.integration.FonoApi
import krasilov.dima.model.DAO
import krasilov.dima.web.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

// fixme better to split to bookingsService and devicesService
class ApiService(private val fonoApi: FonoApi, private val dao: DAO) {
    fun getAllDevices(): List<DeviceInfoExtended> = transaction {
        dao.getAllDevices()
            .map { DeviceInfoExtended(it, fonoApi.getDeviceInfo(it.name)) }
    }

    fun book(bookDevice: BookDevice): Either<Error, DeviceInfoExtended> =
        transaction {
            val (userId, deviceId) = bookDevice

            val device = dao.findDevice(deviceId)
                ?: return@transaction Either.left(Error(ErrorCode.BT00002, "Device Not Found by ID: $deviceId"))

            if (device.available.not()) {
                return@transaction Either.left(Error(ErrorCode.BT00002, "Already booked: $device"))
            }

            dao.bookDevice(userId, deviceId)

            val device1 = dao.findDevice(deviceId)!!
            Either.right(DeviceInfoExtended(device1, fonoApi.getDeviceInfo(device1.name)))
        }

    fun returnDevice(returnDevice: ReturnDevice): Either<Error, DeviceInfoExtended> = transaction {
        val (userId, deviceId) = returnDevice
        val device = dao.findDevice(deviceId)
            ?: return@transaction Either.left(Error(ErrorCode.BT00002, "Device Not Found by ID: $deviceId"))

        if (device.available) {
            return@transaction Either.left(Error(ErrorCode.BT00003, "Device is not booked"))
        }

        if (device.booking?.user?.id != userId) {
            return@transaction Either.left(
                Error(
                    ErrorCode.BT00002,
                    "Device was booked by another user: ${device.booking}"
                )
            )
        }
        val now = LocalDateTime.now()
        dao.returnDevice(device.booking.id, now)
        Either.right(
            DeviceInfoExtended(
                device.copy(
                    booking = device.booking.copy(returnedAt = now.toKotlinLocalDateTime())
                ),
                fonoApi.getDeviceInfo(device.name)
            )
        )
    }

    fun getBookingsHistory(): List<BookingHistoryRecord> = transaction {
        dao.getAllBookings()
    }

}