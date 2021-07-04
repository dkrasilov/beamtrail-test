package krasilov.dima.model

import org.jetbrains.exposed.dao.id.IntIdTable

object Devices : IntIdTable() {
    val name = varchar("name", 64)
}
