package krasilov.dima.web

import kotlinx.serialization.Serializable


@Serializable
data class BookDevice(val userId: Int, val deviceId: Int)

@Serializable
data class ReturnDevice(val userId: Int, val deviceId: Int)