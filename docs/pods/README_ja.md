# ksaypip-pods

[ksaypip](https://github.com/uakihir0/ksaypip) の CocoaPods リポジトリです。`ksaypip-debug` と `ksaypip-release` の2つの Pod を公開しており、それぞれ `core` と `auth` からビルドした `ksaypip` XCFramework を含みます。

```ruby
pod 'ksaypip-release'
```

Swift からは `SaypipFactory`、`SaypipAuthFactory` と各モデルを利用できます。
