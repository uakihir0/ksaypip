> [日本語](./README_ja.md)

# ksaypip.js

This repository is the npm repository for [ksaypip]. [ksaypip] is a [Saypip] client library created using Kotlin Multiplatform.
Therefore, it can also be used in web applications, among others.
Additionally, this repository is automatically committed by GitHub Actions for [ksaypip]. Please submit issues or pull requests to [ksaypip].

The package carries the client (`core`). The OAuth 2.1 flow is not part of the JavaScript
target — obtain an access token out of band (for example, on a server side) and hand it to
`SaypipFactory.instance`.

## Usage

### Installation

If you're managing your project with npm, you can add it to your application using the following command.
There are no versions in this repository, and there are branches that match the versions of [ksaypip].
You can determine which version of [ksaypip] to use by specifying the branch of this repository.
Please check the [branch list](https://github.com/uakihir0/ksaypip.js/branches) to find the branch corresponding to the version.

```shell
npm add uakihir0/ksaypip.js
or
npm add uakihir0/ksaypip.js#{{BRANCH_NAME}}
```

### Requesting

TypeScript type information is included, so it's recommended to use TypeScript.
Please also check the README of [ksaypip] for detailed usage.

```typescript
import { SaypipFactory } from "ksaypip"

const saypip = SaypipFactory.instance("https://saypip.app", accessToken)
const feed = await saypip.feed().feed(new FeedFeedRequest())
```

## License

MIT License

## Author

[Akihiro Urushihara](https://github.com/uakihir0)

[ksaypip]: https://github.com/uakihir0/ksaypip
[Saypip]: https://saypip.app
