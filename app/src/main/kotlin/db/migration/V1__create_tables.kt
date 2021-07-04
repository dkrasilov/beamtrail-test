package db.migration

import krasilov.dima.model.Bookings
import krasilov.dima.model.Devices
import krasilov.dima.model.Users
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class V1__create_tables: BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            SchemaUtils.create(Devices, Users, Bookings)
        }
    }

}