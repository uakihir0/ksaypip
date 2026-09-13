package work.socialhub.ksaypip.auth.api.entity.oauth

import kotlin.js.JsExport

/**
 * One token given up. `token_type_hint` is a hint and nothing more — the server looks the value
 * up either way.
 */
@JsExport
class OAuthRevokeRequest {
    var token: String? = null

    /** `access_token` or `refresh_token`. */
    var tokenTypeHint: String? = null
}
