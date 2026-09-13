package work.socialhub.ksaypip.internal

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.JsonPrimitive

/**
 * A nullable string as a body field: the value, or the explicit null that a request may mean as
 * "clear this" rather than "leave this alone".
 */
fun JsonObjectBuilder.putOrNull(
    key: String,
    value: String?,
) {
    put(key, value?.let { JsonPrimitive(it) } ?: JsonNull)
}
