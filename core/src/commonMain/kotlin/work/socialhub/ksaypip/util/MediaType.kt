package work.socialhub.ksaypip.util

import io.ktor.http.ContentType

object MediaType {

    val JSON = ContentType.Application.Json.toString()

    val WEBP = ContentType("image", "webp").toString()
}
