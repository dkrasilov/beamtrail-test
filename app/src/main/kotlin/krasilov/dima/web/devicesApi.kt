package krasilov.dima.web

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import krasilov.dima.service.ApiService

fun Route.deviceRouting(apiService: ApiService) {
    route("/devices") {
        get {
            val allDevices = apiService.getAllDevices()
            call.respond(GenericResponse.success(allDevices))
        }
    }
}
