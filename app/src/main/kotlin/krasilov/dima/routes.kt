package krasilov.dima

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.deviceRouting() {
    route("/device") {
        get {
            call.respond("Here your devices: blabla")
        }
        post("book") {
            call.respond("Booked!")
        }

        post("return") {
            call.respond("Returned!")
        }
    }
}

fun Application.registerDeviceRoute() {
    routing {
        deviceRouting()
    }
}