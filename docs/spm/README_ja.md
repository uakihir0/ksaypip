> [English](./README.md)

# ksaypip SPM

このリポジトリは [ksaypip] の Swift Package リポジトリです。
[ksaypip] は Kotlin Multiplatform で構築された [Saypip] のクライアントライブラリです。
そのため、iOS などの Apple デバイス上でビルドして利用できます。
ここでは、XCFramework としてビルドしたライブラリを Swift Package で配布しています。
また、このリポジトリは [ksaypip] の GitHub Actions によって自動的にコミットされます。
Issue や Pull Request は [ksaypip] へお願いします。

## 使い方

このリポジトリに独自のバージョニングはありません。
代わりに、[ksaypip] のバージョンに対応したブランチが用意されています。
特定のバージョンの [ksaypip] を使うには、このリポジトリの対応するブランチを指定してください。
使用したいバージョンに対応するブランチは[ブランチ一覧](https://github.com/uakihir0/ksaypip-spm/branches)から確認してください。

### リクエスト

Objective-C でも利用できますが、以下は Swift での利用例です。
詳しい使い方は [ksaypip] の README も参照してください。

```swift
import ksaypip

let auth = SaypipAuthFactory.shared.instance(config: SaypipAuthConfig(
    baseUrl: "https://saypip.app",
    clientId: "saypip_app_...",
    clientSecret: nil,
    redirectUri: "myapp://callback",
    scopes: ["read", "write", "offline_access"],
    authorizationEndpoint: nil,
    tokenEndpoint: nil,
    revokeEndpoint: nil
))
```

## ライセンス

MIT License

## 作者

[Akihiro Urushihara](https://github.com/uakihir0)

[ksaypip]: https://github.com/uakihir0/ksaypip
[Saypip]: https://saypip.app
