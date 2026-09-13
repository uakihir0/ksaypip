package work.socialhub.ksaypip.stream.internal

/**
 * Whether this platform can put an `Authorization` header on a WebSocket handshake.
 *
 * Native and JVM clients can present a token; the browser WebSocket API cannot set a header at
 * all, so on JavaScript the room is a visitor's — which is still a seat, because reading the room
 * never required an account.
 */
internal expect val presentsHandshakeHeader: Boolean
