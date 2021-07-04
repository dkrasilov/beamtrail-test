package krasilov.dima.web

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable
import krasilov.dima.model.Users
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.usersRouting() {
    route("/users") {
        get {
            val users = transaction {
                Users.selectAll().map {
                    User(
                        id = it[Users.id].value,
                        name = it[Users.name]
                    )
                }
            }
            call.respond(users)
        }
    }
}
