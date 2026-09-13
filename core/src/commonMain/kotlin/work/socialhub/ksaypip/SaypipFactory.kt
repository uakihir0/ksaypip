package work.socialhub.ksaypip

import work.socialhub.ksaypip.internal.SaypipImpl
import kotlin.js.JsExport

@JsExport
object SaypipFactory {

    /**
     * Get a request instance.
     *
     * @param uri the deployment's origin, `https://saypip.app`, with no trailing slash
     * @param accessToken a bearer token an authorized application holds, or empty for the
     *   visitor-readable endpoints
     */
    fun instance(
        uri: String,
        accessToken: String = "",
    ): Saypip {
        return SaypipImpl(uri, accessToken)
    }
}
