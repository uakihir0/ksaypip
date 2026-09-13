> [English](../README.md)

# ksaypip

![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo.repsy.io%2Fmvn%2Fuakihir0%2Fpublic%2Fwork%2Fsocialhub%2Fksaypip%2Fcore%2Fmaven-metadata.xml)

![badge-js]
![badge-jvm]
![badge-ios]
![badge-mac]

**ksaypip は [Saypip](https://saypip.app) のクライアントライブラリで、[Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) に対応しています。**
[khttpclient] に依存し、内部で Ktor Client を利用しているため、Kotlin Multiplatform と Ktor Client が対応するプラットフォームで利用できます。

Saypip はセミアノニマスな SNS です。全員が匿名で読み書きし、本物のプロフィールは相互にフレンドになった相手にだけ開示され、関係を終了すればサービスは両者を結び付けられないことを保証します。したがってこのライブラリは汎用の REST ラッパーではありません。視聴者ごとにスコープされた identity トークン、7日間の閲覧ウィンドウ、そして唯一の入口である OAuth 2.1 をモデル化しています。

## モジュール

| モジュール | 内容 |
| --- | --- |
| `core` | API クライアント本体（フィード、投稿、会話、関係、通知、ミュート、メディアなど） |
| `auth` | OAuth 2.1 authorization code フロー（PKCE、refresh、revoke） |
| `all` | 上記2つをまとめ、CocoaPods / SPM 向けにパッケージしたもの。JavaScript ターゲットは PKCE プロバイダに JavaScript ビルドがないため `core` のみを含みます |

## 使い方

Kotlin + Gradle での利用方法は以下の通りです。
**Apple プラットフォームでは `all` モジュールの XCFramework / CocoaPods 出力を参照してください。JavaScript では TypeScript 定義でクライアントを利用でき、トークンは OAuth フローで別途取得します。**
各 API の使い方はテストコードを参考にしてください。

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

### 認証

サードパーティクライアントは、このデプロイの OAuth 2.1 認可サーバーを通して認可されます。アプリケーションの登録は運用者だけが行えるため、`client_id`（サーバーサイドアプリでは `client_secret` も）を運用者から受け取ってください。ネイティブアプリは secret を持たず、PKCE だけで動作します。

```kotlin
val auth = SaypipAuthFactory.instance(
    SaypipAuthConfig(
        baseUrl = "https://saypip.app",
        clientId = "saypip_app_...",
        // パブリッククライアントでは省略。ネイティブアプリに secret を埋め込まないこと。
        clientSecret = null,
        redirectUri = "myapp://callback",
    ),
)

val context = auth.context()

// ブラウザで開く URL。verifier と state は context に生成・保持されます。
val url = auth.oauth().buildAuthorizationUrl(context, BuildAuthorizationUrlRequest())
println(url)
```

利用者がサインインして同意すると、コールバックに `code` と `state` が付いて戻ります。state を検証してから code を交換します。

```kotlin
check(callbackState == context.state)

val tokens = auth.oauth().authorizationCodeToken(
    context,
    OAuthAuthorizationCodeTokenRequest().also { it.code = callbackCode },
).data

val saypip = SaypipFactory.instance("https://saypip.app", tokens.accessToken)
```

refresh token は利用者が `offline_access` に同意したときだけ発行されます。アクセストークンの寿命は1時間で、更新と失効も同じ形です。

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

### 読み込み

アカウントなしの訪問者でも Global Room を読めます。トークンを持たないクライアントは access token に空文字を渡します。

```kotlin
val feed = saypip.feed().feed(FeedFeedRequest()).data
feed.items.forEach { post ->
    println(post.body)
    println(post.author?.label)      // 自分が付けた相手の呼び名。見知らぬ相手では null
    println(post.authorColor)        // author が null のときに描く色
    println(post.readableUntil)      // この視聴者が読めなくなる時刻
}

val page2 = saypip.feed().feed(FeedFeedRequest().also { it.cursor = feed.nextCursor })
```

検索・ハッシュタグ・話しかけ募集・トレンドも同じ形です。

```kotlin
saypip.feed().search(FeedSearchRequest().also { it.q = "ラーメン" })
saypip.feed().tag(FeedTagRequest().also { it.tag = "猫" })
saypip.feed().talk(FeedTalkRequest())
saypip.feed().trends(FeedTrendsRequest())
```

### 書き込み

すべての書き込みは任意の `idempotencyKey` を取ります。指定しておけば、遅い回線での再試行は二重投稿ではなく同じ操作の繰り返しになります。

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

### そのほか

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
        it.data = bytes            // image/webp, image/jpeg, image/png
        it.contentType = "image/webp"
    },
)
saypip.feedback().send(FeedbackSendRequest().also { it.message = "..." })
```

`Saypip.posts()` や `Saypip.feed()` などの各メソッドには JVM / native 向けの `*Blocking` 版もあります。JavaScript では suspend 関数を使います。

## アプリケーションが到達できる範囲

ベアラートークンが呼べるのは API の allowlist に載っているルートだけです。管理画面、`GET /me/sign-in`、`DELETE /me`、push 関連、リアルタイムソケットは cookie 専用で、アプリケーションには訪問者と同じ `not_found` を返します。アプリケーションが見るのは利用者自身のビューであり、視聴者ごとの identity、7日間のウィンドウ、ブロックフィルタはすべて同じです。

## 受け取ったデータの扱い

このプロダクトの保証は「関係が終わった後、提供するものから両者を再特定できない」ことです。ただしそれはこのサービスが返すものについての保証です。アプリケーションはコピーであり、コピーの管理は利用者の責任です。このライブラリで作るなら:

- **ラベル・プロフィール・identity トークンを共有ストレージや長期保存領域に書かないこと。** それらは利用者から見た相手であり、関係が終われば消えます。
- **API レスポンスを共有キャッシュに置かないこと。** どのレスポンスも誰かのビューであり、サービスが `private, no-store` を付けているのはそのためです。
- **安定したユーザー ID を作ろうとしないこと。** そもそも存在しません。同じ人物でも利用者ごとに identity トークンは異なり、本物のプロフィールはフレンドになったときにだけ届きます。

## 開発

```shell
./gradlew :core:jvmTest   # シリアライズ・リクエスト構築・エラーのテスト（オフライン）
./gradlew :auth:jvmTest   # PKCE（RFC 7636）と OAuth フローのテスト（ローカルサーバー）
./gradlew jvmJar          # ネットワーク不要のビルド確認
make build                # core と auth を全ターゲット向けにビルド
```

## ライセンス

MIT License

## 作者

[Akihiro Urushihara](https://github.com/uakihir0)

[khttpclient]: https://github.com/uakihir0/khttpclient
[badge-jvm]: http://img.shields.io/badge/-jvm-DB413D.svg
[badge-js]: http://img.shields.io/badge/-js-F8DB5D.svg
[badge-ios]: http://img.shields.io/badge/-ios-CDCDCD.svg
[badge-mac]: http://img.shields.io/badge/-macos-111111.svg
