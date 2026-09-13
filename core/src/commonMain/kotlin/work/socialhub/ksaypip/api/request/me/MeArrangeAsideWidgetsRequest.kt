package work.socialhub.ksaypip.api.request.me

import kotlin.js.JsExport
import work.socialhub.ksaypip.entity.AsideWidgetItem

/**
 * The whole arrangement of the right-hand column, sent as one list.
 *
 * Every widget has to be present exactly once, each with whether the column draws it. A widget
 * that is turned off keeps its place, so turning it back on returns it there.
 */
@JsExport
class MeArrangeAsideWidgetsRequest {
    var items: Array<AsideWidgetItem>? = null
}
