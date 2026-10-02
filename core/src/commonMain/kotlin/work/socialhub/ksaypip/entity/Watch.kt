package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * One person a reader is keeping an eye on, and which of their two writing modes is kept.
 *
 * The positive twin of a mute. An anonymous entry carries the viewer's own [person] — a token, a
 * label, a mark — and an identified entry carries the public handle and badge; the two are never
 * correlated, and one row never names both modes.
 */
@JsExport
@Serializable
class Watch {

    /** `anonymous` or `identified`. */
    var mode: String = ""

    var person: Person = Person()

    var createdAt: String = ""
}
