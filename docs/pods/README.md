# ksaypip-pods

CocoaPods repository for [ksaypip](https://github.com/uakihir0/ksaypip), the Saypip client
library for Kotlin Multiplatform. Two pods are published, `ksaypip-debug` and `ksaypip-release`,
each carrying the `ksaypip` XCFramework built from the `core` and `auth` modules.

```ruby
pod 'ksaypip-release'
```

The framework exposes `SaypipFactory`, `SaypipAuthFactory` and the models through Swift.
