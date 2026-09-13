package work.socialhub.ksaypip.util

import kotlinx.coroutines.CoroutineScope

/**
 * A suspend call as a blocking one.
 *
 * JVM and native run the block; JavaScript throws, because there is no thread to block.
 */
expect fun <T> toBlocking(block: suspend CoroutineScope.() -> T): T
