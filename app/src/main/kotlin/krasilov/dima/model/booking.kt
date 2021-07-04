package krasilov.dima.model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object Bookings : IntIdTable() {
    val userId = reference("userId", Users.id)
    val deviceId = reference("deviceId", Devices.id)
    val bookedAt = datetime("bookedAt")
    val returnedAt = datetime("returnedAt").nullable()

    init {
        // that's the way to ensure that device cannot be booked twice
        uniqueIndex(deviceId, returnedAt)
    }
}