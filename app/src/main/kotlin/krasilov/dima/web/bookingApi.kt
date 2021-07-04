package krasilov.dima.web

import arrow.core.Either
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import krasilov.dima.service.ApiService

fun Route.bookingRouting(apiService: ApiService) {
    route("/bookings") {
        get {
            call.respond(apiService.getBookingsHistory())
        }

        post {
            val bookDevice = call.receive(BookDevice::class)
            val result = apiService.book(bookDevice)
            call.respondEither(result)
        }

        delete {
            val returnDevice = call.receive(ReturnDevice::class)
            val result = apiService.returnDevice(returnDevice)
            call.respondEither(result)
        }
    }
}


private suspend inline fun <reified T : Any> ApplicationCall.respondEither(result: Either<Error, T>) {
    result.fold(
        ifLeft = { error ->
            respond(HttpStatusCode.fromValue(error.statusCode), GenericResponse.error(error))
        },
        ifRight = { respond(GenericResponse.success(it)) }
    )
}
