package work.socialhub.ksaypip.auth

import work.socialhub.ksaypip.auth.internal.SaypipAuthImpl

object SaypipAuthFactory {

    fun instance(config: SaypipAuthConfig): SaypipAuth {
        return SaypipAuthImpl(config)
    }

    fun instance(
        baseUrl: String,
        clientId: String,
    ): SaypipAuth {
        return instance(
            SaypipAuthConfig(
                baseUrl = baseUrl,
                clientId = clientId,
            ),
        )
    }
}
