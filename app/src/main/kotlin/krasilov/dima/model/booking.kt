package krasilov.dima.model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object Bookings : IntIdTable() {
    val userId = reference("userId", Users.id)
    val deviceId = reference("deviceId", Devices.id)
    val bookedAt = datetime("bookedAt")
    val returnedAt = datetime("returnedAt").nullable()

    init {
        // fixme we actually need to create partial index to prevent booking same device twice
        // unqiue deviceId where returnedAt == null      
        uniqueIndex(deviceId, returnedAt)
    }
}
