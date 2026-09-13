> [日本語](./docs/README_ja.md)

# ksaypip

![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo.repsy.io%2Fmvn%2Fuakihir0%2Fpublic%2Fwork%2Fsocialhub%2Fksaypip%2Fcore%2Fmaven-metadata.xml)

![badge][badge-js]
![badge][badge-jvm]
![badge][badge-ios]
![badge][badge-mac]

**This library is a [Saypip](https://saypip.app) client library that supports [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html).**
It depends on [khttpclient] and internally uses Ktor Client, so it is available on Kotlin Multiplatform and the platforms Ktor Client supports.

Saypip is a semi-anonymous SNS: everyone browses and posts anonymously, real profiles are revealed
only between mutual friends, and ending a friendship is guaranteed to make the two of you
unlinkable. The library is therefore not a generic REST wrapper — it models the product's
viewer-scoped identities, its 7-day reading window, and the one path in: OAuth 2.1.

## Modules

| Module | Contents |
| --- | --- |
| `core` | The API client: reader view, posts, conversations, relationships, notifications, mutes, media, and so on |
| `auth` | OAuth 2.1 authorization code flow with PKCE, refresh and revoke |
| `all` | Both of the above, packaged for CocoaPods / SPM; the JavaScript target carries `core` only, as the PKCE provider has no JavaScript build |

## Usage

Below is how to use it in Kotlin with Gradle on supported platforms.
**If you want to use it on Apple platforms, please refer to the `all` module's XCFramework / CocoaPods output; for JavaScript, its TypeScript definitions cover the client, with the token obtained through the OAuth flow out of band.**
Please refer to the test code for how to use each API.

```kotlin:build.gradle.kts
repositories {
    mavenCentral()
+   maven { url = uri("https://repo.repsy.io/mvn/uakihir0/public") }
}

dependencies {
+   implementation("work.socialhub.ksaypip:core:0.0.1-SNAPSHOT")
+   implementation("work.socialhub.ksaypip:auth:0.0.1-SNAPSHOT")
}
```

### Authentication

Third-party clients are authorized through this deployment's OAuth 2.1 authorization server. An
application is registered by the operator, not by itself: ask the operator for a `client_id` (and,
for a server-side application, a `client_secret`). A native application has no secret and uses
PKCE alone.

```kotlin
val auth = SaypipAuthFactory.instance(
    SaypipAuthConfig(
        baseUrl = "https://saypip.app",
        clientId = "saypip_app_...",
        // Omit for a public client. Never ship a secret inside a native application.
        clientSecret = null,
        redirectUri = "myapp://callback",
    ),
)

val context = auth.context()

// The URL to open in a browser. The verifier and state are generated into the context.
val url = auth.oauth().buildAuthorizationUrl(context, BuildAuthorizationUrlRequest())
println(url)
```

The reader signs in and agrees; the callback arrives with `code` and `state`. Check the state, then
exchange the code:

```kotlin
check(callbackState == context.state)

val tokens = auth.oauth().authorizationCodeToken(
    context,
    OAuthAuthorizationCodeTokenRequest().also { it.code = callbackCode },
).data

val saypip = SaypipFactory.instance("https://saypip.app", tokens.accessToken)
```

A refresh token arrives only when the reader agreed to `offline_access`. Access tokens last an
hour; refresh and revocation work the same way:

```kotlin
val next = auth.oauth().refreshToken(
    context,
    OAuthRefreshTokenRequest().also { it.refreshToken = tokens.refreshToken },
).data

auth.oauth().revoke(
    context,
    OAuthRevokeRequest().also {
        it.token = next.refreshToken
        it.tokenTypeHint = "refresh_token"
    },
)
```

### Reading the room

A visitor can read the Global Room without an account, and so can a client with no token: pass an
empty access token.

```kotlin
val feed = saypip.feed().feed(FeedFeedRequest()).data
feed.items.forEach { post ->
    println(post.body)
    println(post.author?.label)      // the viewer's own name for them, or null for a stranger
    println(post.authorColor)        // the colour to draw instead, where author is null
    println(post.readableUntil)      // when this viewer stops being able to read it
}

val page2 = saypip.feed().feed(FeedFeedRequest().also { it.cursor = feed.nextCursor })
```

Search, a hashtag, the talk timeline and trends are the same shape:

```kotlin
saypip.feed().search(FeedSearchRequest().also { it.q = "ラーメン" })
saypip.feed().tag(FeedTagRequest().also { it.tag = "猫" })
saypip.feed().talk(FeedTalkRequest())
saypip.feed().trends(FeedTrendsRequest())
```

### Writing

Every write takes an optional `idempotencyKey`: give one and a retry on a slow connection is a
repeat, not a second write.

```kotlin
val post = saypip.posts().create(
    PostsCreateRequest().also {
        it.body = "Post from ksaypip!"
        it.idempotencyKey = "a-key-of-your-choosing"
    },
).data

saypip.posts().react(
    PostsReactRequest().also {
        it.postId = post.id
        it.emoji = "🎉"
    },
)

saypip.posts().startConversation(
    PostsStartConversationRequest().also {
        it.postId = post.id
        it.body = "こんにちは"
    },
)
```

### Conversations, relationships and the rest

```kotlin
saypip.conversations().list(ConversationsListRequest())
saypip.conversations().conversation(
    ConversationsConversationRequest().also { it.conversationId = "c_..." },
)
saypip.relationships().list(RelationshipsListRequest())
saypip.friendRequests().list(FriendRequestsListRequest())
saypip.notifications().list(NotificationsListRequest())
saypip.mutes().list(MutesListRequest())
saypip.media().upload(
    MediaUploadRequest().also {
        it.data = bytes            // image/webp, image/jpeg or image/png
        it.contentType = "image/webp"
    },
)
saypip.feedback().send(FeedbackSendRequest().also { it.message = "..." })
```

`Saypip.posts()`, `Saypip.feed()` and the rest each have a `*Blocking` twin for JVM and native
callers; JavaScript uses the suspend functions.

## What an application can reach

A bearer token can call the routes the API's allowlist names, and nothing else. The admin area,
`GET /me/sign-in`, `DELETE /me`, the push endpoints and the realtime socket are cookie-only and
answer an application the same `not_found` a stranger gets. An application sees the reader's own
view — the same viewer-scoped identities, the same 7-day window, the same block filtering.

## Handling what you receive

The product's guarantee is that after a relationship ends, nothing it provides can re-identify
either person. That guarantee is about what this service answers; an application is a copy, and a
copy is the reader's to make. If you build on this library:

- **Do not write labels, profiles or identity tokens to a shared or long-lived store.** They are
  the reader's view of a person, and they die when the relationship does.
- **Do not cache API responses in a shared cache.** Every response is a view of somebody, and the
  service marks them `private, no-store` for that reason.
- **Never try to build a stable user ID.** There is none to build: two readers get different
  identity tokens for the same person, and real profiles arrive only through friendship.

## Development

```shell
./gradlew :core:jvmTest   # serialization, request and error tests
./gradlew :auth:jvmTest   # PKCE and the OAuth flow, against a local server
./gradlew jvmJar          # no network needed
make build                # assemble core and auth for every target
```

## License

MIT License

## Author

[Akihiro Urushihara](https://github.com/uakihir0)

[khttpclient]: https://github.com/uakihir0/khttpclient
[badge-jvm]: http://img.shields.io/badge/-jvm-DB413D.svg
[badge-js]: http://img.shields.io/badge/-js-F8DB5D.svg
[badge-ios]: http://img.shields.io/badge/-ios-CDCDCD.svg
[badge-mac]: http://img.shields.io/badge/-macos-111111.svg
