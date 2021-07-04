package db.migration

import krasilov.dima.model.Devices
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class V2__create_devices : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            listOf(
                "Samsung Galaxy S9",
                "Samsung Galaxy S8",
                "Samsung Galaxy S7",
                "Motorola Nexus 6",
                "LG Nexus 5X",
                "Huawei Honor 7X",
                "Apple iPhone X",
                "Apple iPhone 8",
                "Apple iPhone 4s",
                "Nokia 3310"
            ).forEach { deviceName ->
                Devices.insert {
                    it[name] = deviceName
                }
            }
        }
    }
}