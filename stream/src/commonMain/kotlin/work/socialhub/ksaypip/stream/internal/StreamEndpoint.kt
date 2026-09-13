package work.socialhub.ksaypip.stream.internal

/**
 * The address of the room, from a deployment's origin.
 *
 * `http` becomes `ws` and `https` becomes `wss`, so a local deployment works with the same
 * client; an address that already names a scheme is left as it is, and the path is `/ws`.
 */
internal object StreamEndpoint {

    fun webSocketUrl(uri: String): String {
        val base = uri.trimEnd('/')
        val authority = when {
            base.startsWith("https://") -> "wss://" + base.removePrefix("https://")
            base.startsWith("http://") -> "ws://" + base.removePrefix("http://")
            else -> base
        }
        return "$authority/ws"
    }
}
