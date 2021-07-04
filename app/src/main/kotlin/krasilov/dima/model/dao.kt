package krasilov.dima.model

import kotlinx.datetime.toKotlinLocalDateTime
import krasilov.dima.web.*
import org.jetbrains.exposed.sql.*
import java.time.LocalDateTime

interface DAO {
    fun getAllDevices(): List<DeviceInfo>
    fun findDevice(id: Int): DeviceInfo?
    fun bookDevice(userId: Int, deviceId: Int)
    fun returnDevice(bookingId: Int, returnDate: LocalDateTime)
    fun getAllBookings(): List<BookingHistoryRecord>
}

object DslDAO : DAO {
    override fun getAllDevices(): List<DeviceInfo> =
        Devices
            .join(
                otherTable = Bookings.innerJoin(Users),
                joinType = JoinType.LEFT,
                onColumn = Devices.id,
                otherColumn = Bookings.deviceId,
                additionalConstraint = { Bookings.returnedAt.isNull() }
            )
            .selectAll()
            .map { toDevice(it) }

    override fun findDevice(id: Int): DeviceInfo? = Devices
        .join(
            otherTable = Bookings.innerJoin(Users),
            joinType = JoinType.LEFT,
            onColumn = Devices.id,
            otherColumn = Bookings.deviceId,
            additionalConstraint = { Bookings.returnedAt.isNull() }
        )
        .select { Devices.id eq id }
        .limit(1)
        .map { toDevice(it) }
        .firstOrNull()

    override fun bookDevice(userId: Int, deviceId: Int) {
        Bookings.insert {
            it[bookedAt] = LocalDateTime.now()
            it[Bookings.userId] = userId
            it[Bookings.deviceId] = deviceId
        }
    }

    override fun returnDevice(bookingId: Int, returnDate: LocalDateTime) {
        Bookings.update({ Bookings.id eq bookingId }) {
            it[returnedAt] = returnDate
        }
    }

    override fun getAllBookings(): List<BookingHistoryRecord> =
        Bookings
            .innerJoin(Devices)
            .innerJoin(Users)
            .selectAll()
            .map { row ->
                BookingHistoryRecord(
                    id = row[Bookings.id].value,
                    device = Device(
                        id = row[Devices.id].value,
                        name = row[Devices.name]
                    ),
                    user = User(
                        id = row[Users.id].value,
                        name = row[Users.name]
                    ),
                    bookedAt = row[Bookings.bookedAt].toKotlinLocalDateTime(),
                    returnedAt = row[Bookings.returnedAt]?.toKotlinLocalDateTime()
                )
            }

    private fun toDevice(resultRow: ResultRow): DeviceInfo {
        val deviceName = resultRow[Devices.name]

        val available = resultRow.getOrNull(Bookings.userId)?.value == null

        val booking = if (!available) {
            DeviceBooking(
                id = resultRow[Bookings.id].value,
                user = User(resultRow[Users.id].value, resultRow[Users.name]),
                bookedAt = resultRow[Bookings.bookedAt].toKotlinLocalDateTime(),
                returnedAt = resultRow[Bookings.returnedAt]?.toKotlinLocalDateTime()
            )
        } else null
        return DeviceInfo(
            id = resultRow[Devices.id].value,
            name = deviceName,
            available = available,
            booking = booking
        )
    }
}