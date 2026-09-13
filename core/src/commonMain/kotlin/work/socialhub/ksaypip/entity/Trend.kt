package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * What many people are writing about: the one answer in this API that is the same for every
 * reader.
 *
 * `writers` is a cardinality, never attributable — the accounts are counted and discarded
 * inside one statement.
 */
@JsExport
@Serializable
class Trend {

    /** Normalized, without the `#`. */
    var tag: String = ""

    var writers: Int = 0
}
