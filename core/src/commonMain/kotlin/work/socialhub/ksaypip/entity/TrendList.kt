package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * The trend measurement. Not paged: it is a handful of rows by construction.
 */
@JsExport
@Serializable
class TrendList {

    /** When the count was taken. Null when the list is empty. */
    var measuredAt: String? = null

    var items: Array<Trend> = arrayOf()
}
