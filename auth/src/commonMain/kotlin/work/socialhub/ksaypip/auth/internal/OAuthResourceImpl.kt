package work.socialhub.ksaypip.auth.internal

import io.ktor.http.URLBuilder
import work.socialhub.khttpclient.HttpRequest
import work.socialhub.khttpclient.HttpResponse
import work.socialhub.ksaypip.SaypipException
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.auth.OAuthContext
import work.socialhub.ksaypip.auth.SaypipAuthConfig
import work.socialhub.ksaypip.auth.api.OAuthResource
import work.socialhub.ksaypip.auth.api.entity.oauth.BuildAuthorizationUrlRequest
import work.socialhub.ksaypip.auth.api.entity.oauth.OAuthAuthorizationCodeTokenRequest
import work.socialhub.ksaypip.auth.api.entity.oauth.OAuthRefreshTokenRequest
import work.socialhub.ksaypip.auth.api.entity.oauth.OAuthRevokeRequest
import work.socialhub.ksaypip.auth.api.entity.oauth.OAuthTokenResponse
import work.socialhub.ksaypip.auth.helper.PkceHelper
import work.socialhub.ksaypip.auth.helper.RandomHelper
import work.socialhub.ksaypip.internal.InternalUtility
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class OAuthResourceImpl(
    private val config: SaypipAuthConfig,
) : OAuthResource {

    override fun buildAuthorizationUrl(
        context: OAuthContext,
        request: BuildAuthorizationUrlRequest,
    ): String {
        val clientId = request.clientId ?: context.clientId ?: config.clientId
        val redirectUri = request.redirectUri ?: context.redirectUri ?: config.redirectUri
        val scopes = request.scopes?.toList() ?: context.scopes ?: config.scopes

        if (clientId.isBlank()) throw SaypipException("clientId is required.")
        if (redirectUri.isNullOrBlank()) throw SaypipException("redirectUri is required.")

        val codeVerifier = context.codeVerifier ?: PkceHelper.codeVerifier().also {
            context.codeVerifier = it
        }
        val state = request.state ?: context.state ?: RandomHelper.random(32).also {
            context.state = it
        }

        context.clientId = clientId
        context.redirectUri = redirectUri
        context.scopes = scopes
        context.state = state

        return URLBuilder(config.authorizationUrl).apply {
            parameters.append("response_type", "code")
            parameters.append("client_id", clientId)
            parameters.append("redirect_uri", redirectUri)
            parameters.append("scope", scopes.joinToString(" "))
            parameters.append("state", state)
            parameters.append("code_challenge", PkceHelper.codeChallenge(codeVerifier))
            parameters.append("code_challenge_method", "S256")
        }.buildString()
    }

    override fun authorizationCodeToken(
        context: OAuthContext,
        request: OAuthAuthorizationCodeTokenRequest,
    ): Response<OAuthTokenResponse> {
        val code = request.code ?: throw SaypipException("code is required.")
        val redirectUri = context.redirectUri ?: config.redirectUri
            ?: throw SaypipException("redirectUri is required.")
        val codeVerifier = context.codeVerifier
            ?: throw SaypipException("codeVerifier is missing: build the authorization URL first.")

        return tokenRequest(
            mapOf(
                "grant_type" to "authorization_code",
                "code" to code,
                "redirect_uri" to redirectUri,
                "code_verifier" to codeVerifier,
            ),
        )
    }

    override fun refreshToken(
        context: OAuthContext,
        request: OAuthRefreshTokenRequest,
    ): Response<OAuthTokenResponse> {
        val refreshToken = request.refreshToken ?: throw SaypipException("refreshToken is required.")

        return tokenRequest(
            mapOf(
                "grant_type" to "refresh_token",
                "refresh_token" to refreshToken,
            ),
        )
    }

    override fun revoke(
        context: OAuthContext,
        request: OAuthRevokeRequest,
    ): ResponseUnit {
        return toBlocking {
            proceedUnit {
                HttpRequest()
                    .url(config.revokeUrl)
                    .accept(MediaType.JSON)
                    .pwn("client_id", context.clientId ?: config.clientId)
                    .pwn("client_secret", config.clientSecret)
                    .pwn("token", request.token)
                    .pwn("token_type_hint", request.tokenTypeHint)
                    .forceApplicationFormUrlEncoded(true)
                    .post()
            }
        }
    }

    private fun tokenRequest(
        fields: Map<String, String>,
    ): Response<OAuthTokenResponse> {
        return toBlocking {
            proceed {
                HttpRequest()
                    .url(config.tokenUrl)
                    .accept(MediaType.JSON)
                    .pwn("client_id", config.clientId)
                    .pwn("client_secret", config.clientSecret)
                    .also { request ->
                        fields.forEach { (key, value) -> request.pwn(key, value) }
                    }
                    .forceApplicationFormUrlEncoded(true)
                    .post()
            }
        }
    }

    private fun HttpRequest.pwn(
        key: String,
        value: Any?,
    ): HttpRequest {
        if (value != null) param(key, value)
        return this
    }

    private suspend inline fun <reified T> proceed(
        function: suspend () -> HttpResponse,
    ): Response<T> {
        try {
            val response = function()
            if (response.status in 200..299) {
                return Response(InternalUtility.fromJson<T>(response.stringBody)).also {
                    it.json = response.stringBody
                    it.status = response.status
                }
            }
            throw InternalUtility.errorOf(response.status, response.stringBody)
        } catch (e: Exception) {
            throw e as? SaypipException ?: SaypipException(e)
        }
    }

    private suspend inline fun proceedUnit(
        function: suspend () -> HttpResponse,
    ): ResponseUnit {
        try {
            val response = function()
            if (response.status in 200..299) {
                return ResponseUnit().also {
                    it.json = response.stringBody
                    it.status = response.status
                }
            }
            throw InternalUtility.errorOf(response.status, response.stringBody)
        } catch (e: Exception) {
            throw e as? SaypipException ?: SaypipException(e)
        }
    }
}
