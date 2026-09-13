> [日本語](./README_ja.md)

# ksaypip SPM

This repository is the Swift Package repository for [ksaypip].
[ksaypip] is a [Saypip] client library built using Kotlin Multiplatform.
As a result, it can be built and used on Apple devices such as iOS.
Here, we distribute the library built as an XCFramework via Swift Package.
Additionally, this repository is automatically committed to via GitHub Actions from [ksaypip].
Please direct any issues or pull requests to [ksaypip].

## Usage

This repository does not have its own versioning.
Instead, branches corresponding to the versions of [ksaypip] are provided.
To use a specific version of [ksaypip], specify the corresponding branch of this repository.
Check the [list of branches](https://github.com/uakihir0/ksaypip-spm/branches) to find the branch matching your desired version.

### How to Make Requests

It is also possible to use in Objective-C, but the following is the usage in Swift.
Please also check the README of [ksaypip] for detailed usage.

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

## License

MIT License

## Author

[Akihiro Urushihara](https://github.com/uakihir0)

[ksaypip]: https://github.com/uakihir0/ksaypip
[Saypip]: https://saypip.app
