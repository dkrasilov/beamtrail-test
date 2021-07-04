package db.migration

import krasilov.dima.model.Users
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class V3__create_users : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            listOf(
                "Michael",
                "John",
                "Ivan",
                "Sergio"
            ).forEach { userName ->
                Users.insert {
                    it[name] = userName
                }
            }
        }
    }
}