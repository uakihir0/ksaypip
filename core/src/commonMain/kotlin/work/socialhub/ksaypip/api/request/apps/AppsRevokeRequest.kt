package work.socialhub.ksaypip.api.request.apps

import kotlin.js.JsExport

/**
 * End one application's access: the consent and every token it authorized go together.
 */
@JsExport
class AppsRevokeRequest {
    var consentId: String? = null
    var idempotencyKey: String? = null
}
