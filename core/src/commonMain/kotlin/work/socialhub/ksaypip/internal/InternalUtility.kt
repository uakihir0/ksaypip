package work.socialhub.ksaypip.internal

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import work.socialhub.ksaypip.SaypipException

object InternalUtility {

    @OptIn(ExperimentalSerializationApi::class)
    val json = Json {
        explicitNulls = false
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    inline fun <reified T> toJson(obj: T): String {
        return json.encodeToString(obj)
    }

    inline fun <reified T> fromJson(obj: String): T {
        return json.decodeFromString(obj)
    }

    /**
     * A non-2xx body as an exception, with `error.code` and `error.reason` read out of it where
     * the body is one of this API's error envelopes.
     */
    fun errorOf(status: Int, body: String): SaypipException {
        val exception = SaypipException(status, body)
        runCatching {
            val error = json.parseToJsonElement(body)
                .jsonObject["error"]
                ?.jsonObject
                ?: return@runCatching

            exception.code = error["code"]?.jsonPrimitive?.contentOrNull
            exception.reason = error["reason"]?.jsonPrimitive?.contentOrNull
        }
        return exception
    }

    private const val HEX = "0123456789ABCDEF"

    /**
     * A path segment, percent-encoded so that a tag or an emoji survives the trip in a URL.
     */
    fun urlEncode(value: String): String {
        val bytes = value.encodeToByteArray()
        val builder = StringBuilder()
        for (byte in bytes) {
            val code = byte.toInt() and 0xFF
            val char = code.toChar()
            if (char in 'A'..'Z' || char in 'a'..'z' || char in '0'..'9' ||
                char == '-' || char == '.' || char == '_' || char == '~'
            ) {
                builder.append(char)
            } else {
                builder.append('%')
                builder.append(HEX[(code shr 4) and 0xF])
                builder.append(HEX[code and 0xF])
            }
        }
        return builder.toString()
    }
}
