package work.socialhub.ksaypip.api.request.apps

import kotlin.js.JsExport

/**
 * The icon an operator imported for a registered application, always WebP bytes.
 */
@JsExport
class AppsIconRequest {
    var clientId: String? = null
}
