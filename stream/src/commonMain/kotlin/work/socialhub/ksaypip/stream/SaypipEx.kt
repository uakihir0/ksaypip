package work.socialhub.ksaypip.stream

import work.socialhub.ksaypip.Saypip

object SaypipEx {

    /**
     * Get the stream resource of this client.
     *
     * The room is the one stream this API has: a single global room, for visitors and accounts
     * alike, from which only the reader's blocks and mutes are subtracted.
     */
    fun Saypip.stream(): StreamResource {
        return StreamResourceImpl(uri(), accessToken())
    }
}
