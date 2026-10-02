package work.socialhub.ksaypip.internal

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import work.socialhub.khttpclient.HttpRequest

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

/**
 * The `SetLabelRequest` body, shared by the two addresses that write the same row: a relationship
 * and an identity token. `colors` is the gradient's two ends, or the explicit null that clears it.
 */
fun HttpRequest.labelBody(
    label: String?,
    note: String?,
    markEmoji: String?,
    markColors: Array<String>?,
): HttpRequest {
    return json(
        buildJsonObject {
            put("label", label?.let { JsonPrimitive(it) } ?: JsonNull)
            put("note", note?.let { JsonPrimitive(it) } ?: JsonNull)
            put(
                "mark",
                buildJsonObject {
                    put("emoji", markEmoji?.let { JsonPrimitive(it) } ?: JsonNull)
                    put(
                        "colors",
                        markColors?.let { colors ->
                            buildJsonArray { colors.forEach { add(JsonPrimitive(it)) } }
                        } ?: JsonNull,
                    )
                },
            )
        }.toString(),
    )
}
