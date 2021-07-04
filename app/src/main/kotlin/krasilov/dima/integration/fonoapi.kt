package krasilov.dima.integration

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.serialization.Serializable

interface FonoApi {
    fun getDeviceInfo(name: String): FonoApiDeviceInfo
}

/**
 * Real API is down so i'm mocking it
 */
object MockFonoApi : FonoApi {
    override fun getDeviceInfo(name: String): FonoApiDeviceInfo {
        return FonoApiDeviceInfo("234")
    }
}

object KtorFonoApi : FonoApi {
    val client = HttpClient(CIO)

    override fun getDeviceInfo(name: String): FonoApiDeviceInfo {
        TODO("Not yet implemented")
    }

    private fun getToken(): String {
        TODO("Not yet implemented")
    }
}

@Serializable
data class FonoApiDeviceInfo(
    val s: String
)