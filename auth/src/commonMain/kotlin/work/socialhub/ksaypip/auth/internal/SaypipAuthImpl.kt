package work.socialhub.ksaypip.auth.internal

import work.socialhub.ksaypip.auth.OAuthContext
import work.socialhub.ksaypip.auth.SaypipAuth
import work.socialhub.ksaypip.auth.SaypipAuthConfig
import work.socialhub.ksaypip.auth.api.OAuthResource

class SaypipAuthImpl(
    val config: SaypipAuthConfig,
) : SaypipAuth {

    private val context = OAuthContext()

    private val oauth: OAuthResource = OAuthResourceImpl(config)

    override fun oauth() = oauth

    override fun context() = context
}
