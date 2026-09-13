package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * The right-hand column as arranged: every widget the product has, in the reader's order.
 */
@JsExport
@Serializable
class AsideWidgets {

    var items: Array<AsideWidgetItem> = arrayOf()
}
