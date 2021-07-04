package krasilov.dima

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.netty.*
import krasilov.dima.integration.MockFonoApi
import krasilov.dima.model.DatabaseFactory
import krasilov.dima.model.DslDAO
import krasilov.dima.service.ApiService
import krasilov.dima.web.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)

    install(ContentNegotiation) {
        json()
    }

    install(StatusPages) {
        exception<IllegalArgumentException> { cause ->
            call.respond(HttpStatusCode.BadRequest, GenericResponse.error(ErrorCode.BT00003, cause.localizedMessage))
        }
    }

    DatabaseFactory.connectAndMigrate()

    val apiService = ApiService(MockFonoApi, DslDAO)

    install(Routing) {
        bookingRouting(apiService)
        deviceRouting(apiService)
        usersRouting()
    }
}