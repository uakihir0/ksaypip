package work.socialhub.ksaypip

import kotlin.js.JsExport

/**
 * The one object a JavaScript or Swift consumer needs: the client, and the OAuth factory beside
 * it through the `auth` module's own export.
 */
@JsExport
object KsaypipFactory {

    fun instance(
        uri: String,
        accessToken: String = "",
    ) = SaypipFactory.instance(uri, accessToken)
}
