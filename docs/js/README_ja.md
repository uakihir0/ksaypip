> [English](./README.md)

# ksaypip.js

このリポジトリは [ksaypip] の npm リポジトリです。[ksaypip] は Kotlin Multiplatform で作成された [Saypip] のクライアントライブラリです。
そのため、Web アプリケーションなどでも利用できます。
また、このリポジトリは [ksaypip] の GitHub Actions によって自動的にコミットされます。Issue や Pull Request は [ksaypip] へお願いします。

このパッケージにはクライアント（`core`）と OAuth 2.1 のフロー（`auth`）が含まれます。PKCE プロバイダは WebCrypto のため、authorization code フローはブラウザと Node 18+ で動作します。

## 使い方

### インストール

npm でプロジェクトを管理している場合、以下のコマンドでアプリケーションに追加できます。
このリポジトリにバージョンはなく、[ksaypip] のバージョンに対応したブランチがあります。
このリポジトリのブランチを指定することで、使用する [ksaypip] のバージョンを決められます。
対応するバージョンは[ブランチ一覧](https://github.com/uakihir0/ksaypip.js/branches)から確認してください。

```shell
npm add uakihir0/ksaypip.js
または
npm add uakihir0/ksaypip.js#{{BRANCH_NAME}}
```

### リクエスト

TypeScript の型情報が含まれているため、TypeScript での利用を推奨します。
詳しい使い方は [ksaypip] の README も参照してください。

```typescript
import { SaypipFactory } from "ksaypip"

const saypip = SaypipFactory.instance("https://saypip.app", accessToken)
const feed = await saypip.feed().feed(new FeedFeedRequest())
```

## ライセンス

MIT License

## 作者

[Akihiro Urushihara](https://github.com/uakihir0)

[ksaypip]: https://github.com/uakihir0/ksaypip
[Saypip]: https://saypip.app
