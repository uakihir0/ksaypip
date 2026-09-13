package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * One person a reader is hiding, and until when. `endsAt` null is forever.
 */
@JsExport
@Serializable
class Mute {

    var person: Person = Person()

    /** Null means until taken back. */
    var endsAt: String? = null

    var createdAt: String = ""
}
